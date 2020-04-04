###   

```
Thread{
    Looper.prepare()
    Looper.looper;
    Handler().post(Runnable {  })
}
```

1. Looper.prepare()
```
private static void prepare(boolean quitAllowed) {
        if (sThreadLocal.get() != null) {//当前线程Looper对象为null,则抛出运行时异常
            throw new RuntimeException("Only one Looper may be created per thread");
        }
        
        sThreadLocal.set(new Looper(quitAllowed));
    }
```

2. 单线程单Lopper的情况下，Looper构造的消息队列也是单线程唯一的,<font color=red>总结 : 单个线程仅能拥有一个Lopper和一个MessageQueue.</font>
```
 private Looper(boolean quitAllowed) {
        mQueue = new MessageQueue(quitAllowed);
        mThread = Thread.currentThread();
    }
```

3. looper.loop()  不断的从消息队列取消息,并将消息进行分发
```
 public static void loop() {
 
    //保证调用loop时当前线程有Looper对象
    final Looper me = myLooper();
        if (me == null) {
            throw new RuntimeException("No Looper; Looper.prepare() wasn't called on this thread.");
        }
        
    //获取当前线程的消息队列    
    final MessageQueue queue = me.mQueue;
    
    //开启无限循环
     for (;;) {
            //从队列中拉取下一条消息,具体怎么取消息后面看
            Message msg = queue.next(); // might block 
            
            //后续可以看到MessageQueue里的说明,返回null的原因是消息循环已经退出并被释放.所以这里也就退出Loop的无限循环了.
            if (msg == null) {
                // No message indicates that the message queue is quitting.
                return;
            }
            
            final long dispatchEnd;
            
            //从message中取出发送消息的handler.并调用dispatchMessage()方法
            try {
                msg.target.dispatchMessage(msg);
                dispatchEnd = needEndTime ? SystemClock.uptimeMillis() : 0;
            } finally {
                if (traceTag != 0) {
                    Trace.traceEnd(traceTag);
                }
            }      
     }
 }
```

4. msg.target.dispatchMessage(msg);   其中msg.target指的是原发送消息的Handler.  
```
 public void dispatchMessage(Message msg) {
        if (msg.callback != null) {//首先判断Handler.post()时传递进来的callback是否为null，不为null则执行该回调，第6点会说道该回调的注入
            handleCallback(msg);
        } else {
            if (mCallback != null) {//否则则判断Handler构建时传递的回调是否为null，不为null则执行,第5点会说道该回调的注入
                if (mCallback.handleMessage(msg)) {
                    return;
                }
            }
            
            //如果上面两个都为null的话,则执行Handler的handleMessage(Message msg)方法，该方法是null方法，可以被子类Handler复写,
            handleMessage(msg);
        }
    }
```

5. 看完上面再看Handler的构造函数(已忽略部分代码)
```
 public Handler(Callback callback, boolean async) {
        mLooper = Looper.myLooper();  //通过ThreadLocal获取当前线程的Looper对象
        
        if (mLooper == null) {//如果当前线程没有Looper对象,那么会提示当前线程必须执行Looper.prepare()
            throw new RuntimeException("Can't create handler inside thread " + Thread.currentThread()+ " that has not called Looper.prepare()");
        }
        
        mQueue = mLooper.mQueue;//持有Looper构造时生成的队列
        mCallback = callback;   //创建Handler的时候,可以注入一个消息处理回调.
        mAsynchronous = async;
    }
```

6. 接下来看看Handler.post流程这里会把Runnable封装到Message中,并将Message通过enqueueMessage(queue, msg, uptimeMillis);将Message发送到当前线程的MessageQueue中
```
public final boolean post(Runnable r){
       return  sendMessageDelayed(getPostMessage(r), 0);
}

private static Message getPostMessage(Runnable r) {
    Message m = Message.obtain();
    m.callback = r;
    return m;
}

public final boolean sendMessageDelayed(Message msg, long delayMillis){
    if (delayMillis < 0) {
        delayMillis = 0;
    }
    return sendMessageAtTime(msg, SystemClock.uptimeMillis() + delayMillis);
}

public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
    MessageQueue queue = mQueue;
    if (queue == null) {
        //构造Handler的时候,会持有当前线程的Looper和Message，发消息的时候会判断当前线程是否有消息队列,没有会抛出异常
        RuntimeException e = new RuntimeException(this + " sendMessageAtTime() called with no mQueue");
        
        Log.w("Looper", e.getMessage(), e);
        return false;
    }
    return enqueueMessage(queue, msg, uptimeMillis);
}
```

7. 
```
private boolean enqueueMessage(MessageQueue queue, Message msg, long uptimeMillis) {
        msg.target = this; //让消息持有当前Handler对象,后续处理消息时,会从msg中取出该对象，并msg.callback方法.
        if (mAsynchronous) {
            msg.setAsynchronous(true);
        }
        return queue.enqueueMessage(msg, uptimeMillis);
    }

boolean enqueueMessage(Message msg, long when) {
        synchronized (this) {
            msg.markInUse();
            Message p = mMessages;
            boolean needWake;
            if (p == null || when == 0 || when < p.when) {
                // New head, wake up the event queue if blocked.
                msg.next = p;
                mMessages = msg;
                needWake = mBlocked;
            } else {
                // Inserted within the middle of the queue.  Usually we don't have to wake
                // up the event queue unless there is a barrier at the head of the queue
                // and the message is the earliest asynchronous message in the queue.
                needWake = mBlocked && p.target == null && msg.isAsynchronous();
                Message prev;
                for (;;) {
                    prev = p;
                    p = p.next;
                    if (p == null || when < p.when) {
                        break;
                    }
                    if (needWake && p.isAsynchronous()) {
                        needWake = false;
                    }
                }
                msg.next = p; // invariant: p == prev.next
                prev.next = msg;
            }

            // We can assume mPtr != 0 because mQuitting is false.
            if (needWake) {
                nativeWake(mPtr);
            }
        }
        return true;
    }
```

8
```
  Message next() {
        //消息循环已经退出并被释放
        //如果应用在Looper退出后尝试重启,这是不被允许的
        final long ptr = mPtr;
        if (ptr == 0) {
            return null;
        }

        int pendingIdleHandlerCount = -1; // -1 only during first iteration
        int nextPollTimeoutMillis = 0;
        for (;;) {
            if (nextPollTimeoutMillis != 0) {
                Binder.flushPendingCommands();
            }

            nativePollOnce(ptr, nextPollTimeoutMillis);

            synchronized (this) {
                // Try to retrieve the next message.  Return if found.
                final long now = SystemClock.uptimeMillis();
                Message prevMsg = null;
                Message msg = mMessages;
                if (msg != null && msg.target == null) {
                    // Stalled by a barrier.  Find the next asynchronous message in the queue.
                    do {
                        prevMsg = msg;
                        msg = msg.next;
                    } while (msg != null && !msg.isAsynchronous());
                }
                if (msg != null) {
                    if (now < msg.when) {
                        // Next message is not ready.  Set a timeout to wake up when it is ready.
                        nextPollTimeoutMillis = (int) Math.min(msg.when - now, Integer.MAX_VALUE);
                    } else {
                        // Got a message.
                        mBlocked = false;
                        if (prevMsg != null) {
                            prevMsg.next = msg.next;
                        } else {
                            mMessages = msg.next;
                        }
                        msg.next = null;
                        if (DEBUG) Log.v(TAG, "Returning message: " + msg);
                        msg.markInUse();
                        return msg;
                    }
                } else {
                    // No more messages.
                    nextPollTimeoutMillis = -1;
                }

                // Process the quit message now that all pending messages have been handled.
                if (mQuitting) {
                    dispose();
                    return null;
                }

                // If first time idle, then get the number of idlers to run.
                // Idle handles only run if the queue is empty or if the first message
                // in the queue (possibly a barrier) is due to be handled in the future.
                if (pendingIdleHandlerCount < 0
                        && (mMessages == null || now < mMessages.when)) {
                    pendingIdleHandlerCount = mIdleHandlers.size();
                }
                if (pendingIdleHandlerCount <= 0) {
                    // No idle handlers to run.  Loop and wait some more.
                    mBlocked = true;
                    continue;
                }

                if (mPendingIdleHandlers == null) {
                    mPendingIdleHandlers = new IdleHandler[Math.max(pendingIdleHandlerCount, 4)];
                }
                mPendingIdleHandlers = mIdleHandlers.toArray(mPendingIdleHandlers);
            }

            // Run the idle handlers.
            // We only ever reach this code block during the first iteration.
            for (int i = 0; i < pendingIdleHandlerCount; i++) {
                final IdleHandler idler = mPendingIdleHandlers[i];
                mPendingIdleHandlers[i] = null; // release the reference to the handler

                boolean keep = false;
                try {
                    keep = idler.queueIdle();
                } catch (Throwable t) {
                    Log.wtf(TAG, "IdleHandler threw exception", t);
                }

                if (!keep) {
                    synchronized (this) {
                        mIdleHandlers.remove(idler);
                    }
                }
            }

            // Reset the idle handler count to 0 so we do not run them again.
            pendingIdleHandlerCount = 0;

            // While calling an idle handler, a new message could have been delivered
            // so go back and look again for a pending message without waiting.
            nextPollTimeoutMillis = 0;
        }
    }
```