**如何使用AndroidStudio一步步打造自己的APT框架**

####项目搭建

首先创建一个Android项目


![创建工程.png](http://upload-images.jianshu.io/upload_images/1387450-9fae0d49d8fb3704.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


然后给我们的项目增加一个module,一定要记得是Java Library.
因为APT需要用到jdk下的 【 *javax.~ *】包下的类，这在AndroidSdk中是没有的。


####自定义注解

新建一个类，GetMsg。就是我们自定义的注解。


![anno_lid创建.jpg](http://upload-images.jianshu.io/upload_images/1387450-872e052263469af4.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



####创建Processor
Processor是用来处理Annotation的类。继承自AbstractProcessor。


![progressor.jpg](http://upload-images.jianshu.io/upload_images/1387450-d26a32d66ff0f377.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


复写AbstractProcessor两个最重要的方法。
*process*方法是用来处理注解的，我们一会写。
*getSupportedAnnotationTypes*用来表示该Processor处理哪些注解。这里我们只有一个*GetMsg*注解需要处理。


####重写process方法

我们的目的呢，是获取修饰了GetMsg注解的方法所有信息，只有获得了这些信息，才有依据生成代码不是吗?

		   //process方法是用来处理注解的
			@Override
			public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
				Messager messager = processingEnv.getMessager();
				for (Element element : roundEnv.getElementsAnnotatedWith(GetMsg.class)) {
					PackageElement packageElement = (PackageElement) element
							.getEnclosingElement();
					//获取该注解所在类的包名
					String packageName = packageElement.getQualifiedName().toString();
					TypeElement classElement = (TypeElement) element;
					//获取该注解所在类的类名
					String className = classElement.getSimpleName().toString();
					//获取该注解所在类的全类名
					String fullClassName = classElement.getQualifiedName().toString();
					VariableElement variableElement = (VariableElement) element.getEnclosingElement();
					//获取方法名
					String methodName = variableElement.getSimpleName().toString();
					//获取该注解的值
					int id = classElement.getAnnotation(GetMsg.class).id();
					String name = classElement.getAnnotation(GetMsg.class).name();
					messager.printMessage(Diagnostic.Kind.NOTE,
							"Annotation class : packageName = " + packageName);
					messager.printMessage(Diagnostic.Kind.NOTE,
							"Annotation class : className = " + className);
					messager.printMessage(Diagnostic.Kind.NOTE,
							"Annotation class : fullClassName = " + fullClassName);
					messager.printMessage(Diagnostic.Kind.NOTE,
							"Annotation class : methodName = " + methodName);
					messager.printMessage(Diagnostic.Kind.NOTE,
							"Annotation class : id = " + id + "  name = " + name);
				}
				return true;
			}


简单介绍一下代码：

1.Messager 用来输出。就像我们平时用的System.out.pringln()和Log.d。输出位置在编译器下方的Messages窗口。这里System.out也是可以用的哦~

2.用for循环遍历所有的 GetMsg注解，然后进行处理。

3.Diagnostic.Kind.NOTE 类似于Log.d Log.e这样的等级。

4.return true;表示该Process已经处理了，其他的Process不需要再处理了。


####配置

一定不能忘记的文件配置。

在main文件夹下创建一个resources.META-INF.services文件夹，创建文件

javax.annotation.processing.Processor


![配置文件.jpg](http://upload-images.jianshu.io/upload_images/1387450-62f8e23ed3c25c30.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

文件内容是Process类的包名+类名

![配置内容.jpg](http://upload-images.jianshu.io/upload_images/1387450-b7a424b3eaea9678.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

忘记这个配置文件的后果就是，注解无法生效。



####编译jar

这里有一个坑，我们的主Module是不可以直接引用这个java Module的。（直接引用，可以成功运行一次~修改代码以后就不能运行了）

而如何单独编译这个java Module呢？

在编译器Gradle视图里，找到Module apt下的build目录下的Build按钮。双击运行。




![apt-build允许.jpg](http://upload-images.jianshu.io/upload_images/1387450-720ee328d0f67e2b.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

代码没有问题编译通过的话，会有BUILD SUCCESS提示


![build成功.jpg](http://upload-images.jianshu.io/upload_images/1387450-18182f0f3a1a31b6.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

生成的jar包在 apt 下的build目录下的libs下


![jar目录.jpg](http://upload-images.jianshu.io/upload_images/1387450-7e9127548e4f6f8f.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

将apt.jar拷贝到app下的libs目录，右键该jar，点击Add as Library，添加Library.
在APP项目中使用该注解GetMsg。运行。


**当你apt这个包的代码有修改时，需要重复2.6这个步骤。这是比较烦的，但是没办法**
[](https://lizhaoxuan.github.io/2016/07/17/apt-run_demo/#运行结果)

####运行结果



![运行成功.jpg](http://upload-images.jianshu.io/upload_images/1387450-6076729119930729.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


哈哈，项目终于跑起来啦!
[项目地址](https://github.com/gujingjing/gjj_apt_rundemo.git)
