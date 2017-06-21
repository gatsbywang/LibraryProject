#### 一、前言
 IPC进程间通讯主要作用于两个进程间的信息交互，可以认为是桥梁。Android应用普遍存储应用的服务存活于系统中，为的是随时随地的唤醒app，或者进行推送，IM的信息交互等等功能。这都普遍需要IPC进程通讯，本篇文章将从简单案例，源码，架构进行实战。
####  二、初识IPC
桥梁：AIDL

```
// UserAidl.aidl
package com.demo.ipcdemo;

// Declare any non-default types here with import statements

interface UserAidl {
    String getUserName();
    String getUserPwd();
}

```
可以通过AS创建AIDL文件。

服务端：service

```
public class MessageService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //绑定
        return mBinder;
    }

    private final UserAidl.Stub mBinder = new UserAidl.Stub() {

        @Override
        public String getUserName() throws RemoteException {
            return "Darren@163.com";
        }

        @Override
        public String getUserPwd() throws RemoteException {
            return "19940223";
        }
    };
}
```
其中的stub，可以认为是存储信息的对象，用于发送给客户端的。
将存储对象mBinder存储好信息后，通过onBind方法将其绑定到桥梁上。

客户端：Activity

```
 // 客户端一定要获取aild的实例
    private UserAidl mUserAidl;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //已连接
            mUserAidl = UserAidl.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //断开连接
        }
    };

 @Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, MessageService.class));

        //隐式意图
        Intent intent = new Intent(this, MessageService.class);

        bindService(intent, mServiceConnection, Service.BIND_AUTO_CREATE);
    }
```
首先先启动服务端Service,并将服务端和客户端绑定。绑定之后，通过onServiceConnected接受到服务端传过来的存储对象service，将其解析成客户端所需要的存储对象mUserAidl，然后可通过mUserAidl去获取到服务端service传递过来的信息。这就IPC简单使用。

#### 三、自动生成的AIDL.java

```
public interface UserAidl extends android.os.IInterface {
    /**
     * Local-side IPC implementation stub class.
     */
     
     //Stub类 就说我们上面所说的存储对象
    public static abstract class Stub extends android.os.Binder implements com.demo.ipcdemo.UserAidl {
        private static final java.lang.String DESCRIPTOR = "com.demo.ipcdemo.UserAidl";

        /**
         * Construct the stub at attach it to the interface.
         */
         
         
        public Stub() {
            this.attachInterface(this, DESCRIPTOR);
        }

        /**
         * Cast an IBinder object into an com.demo.ipcdemo.UserAidl interface,
         * generating a proxy if needed.
         */
         
         //客户端activity通过获取到IBinder对象，调用此方法得到我们自定义的Aidl对象。
        public static com.demo.ipcdemo.UserAidl asInterface(android.os.IBinder obj) {
            if ((obj == null)) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (((iin != null) && (iin instanceof com.demo.ipcdemo.UserAidl))) {
                return ((com.demo.ipcdemo.UserAidl) iin);
            }
            //返回的是一个Proxy
            return new com.demo.ipcdemo.UserAidl.Stub.Proxy(obj);
        }

        @Override
        public android.os.IBinder asBinder() {
            return this;
        }

        @Override
        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            switch (code) {
                case INTERFACE_TRANSACTION: {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                case TRANSACTION_getUserName: {
                    data.enforceInterface(DESCRIPTOR);
                    //将返回的username 写入到reply
                    java.lang.String _result = this.getUserName();
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                }
                case TRANSACTION_getUserPwd: {
                    data.enforceInterface(DESCRIPTOR);
                    //将返回的UserPwd 写入到reply
                    java.lang.String _result = this.getUserPwd();
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }


        //Proxy 通过asInterface得到的一个proxy对象，也是一个aidl对象。
        private static class Proxy implements com.demo.ipcdemo.UserAidl {
            private android.os.IBinder mRemote;

            Proxy(android.os.IBinder remote) {
                mRemote = remote;
            }

            @Override
            public android.os.IBinder asBinder() {
                return mRemote;
            }

            public java.lang.String getInterfaceDescriptor() {
                return DESCRIPTOR;
            }

            //通过得到的aidl对象调用getUserName可用得到相应的信息
            @Override
            public java.lang.String getUserName() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                java.lang.String _result;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    //将servic返回的username 写入_reply 中，transact最终会调用的是stub中onTransact方法，
                    mRemote.transact(Stub.TRANSACTION_getUserName, _data, _reply, 0);
                    //将_reply中的数据读取，并返回给客户端
                    _reply.readException();
                    _result = _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }


            //通过得到的aidl对象调用getUserPwd可用得到相应的信息
            @Override
            public java.lang.String getUserPwd() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                java.lang.String _result;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                     //将servic返回的UserPwd 写入_reply 中，transact
                    mRemote.transact(Stub.TRANSACTION_getUserPwd, _data, _reply, 0);
                    //将_reply中的数据读取，并返回给客户端
                    _reply.readException();
                    _result = _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
        }

        static final int TRANSACTION_getUserName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
        static final int TRANSACTION_getUserPwd = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    }

    public java.lang.String getUserName() throws android.os.RemoteException;

    public java.lang.String getUserPwd() throws android.os.RemoteException;
}
```
总体过程为：服务端将stub对象返回给客户端，客户端通过asInterface方法的得到一个Proxy对象，Proxy对象调用getUserName的时候，Proxy对象将存储的服务端传递的stub对象去调用其onTransact方法将信息存储到Proxy对象的_reply中，然后Proxy对象再将其_reply中的信息取出来，返回给客户端使用。

#### 四、总结
一言不合就画图
```
sequenceDiagram
服务端->>客户端: 传递stub对象
客户端->>服务端: 通过stub对象回调onTransact，将信息存储到_reply中
客户端->>客户端: proxy将_reply信息取出返回给客户端使用
```
