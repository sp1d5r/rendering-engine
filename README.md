# 3D Rendering Engine

This is my attempt at building the rendering engine for the last
project... 

I don't know how it'll go but i fucking hate java... Hoping i fall in love here...

###  Known Errors 
LWJGL needs to be run with the VM options:  `-XstartOnFirstThread` otherwise it'll give the following error

```
Caused by: java.lang.IllegalStateException: GLFW may only be used on the main thread and that thread must be the first thread in the process. Please run the JVM with -XstartOnFirstThread. This check may be disabled with Configuration.GLFW_CHECK_THREAD0. ```
```

To do this in intellij click on main and then click on edit configurations, 
then click on Modify Options. Then add Add VM Options. In that field add the  -XstartOnFirstThread.  