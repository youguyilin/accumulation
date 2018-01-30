## Android View
> This class represents the basic building block for user interface components.A View occupies a rectangular area on the screen and is respongsible for drawing and event handling.
* View是用户接口组件的基本构建块。在Android 中一个用户与一个应用的交互，其实就是许许多多View的交互，可以是简单的View，也可以是若干View组成的复合View。所以基本构建块，就是复合View的基本组成单元。
* 那么怎么交互，在哪交互？View在屏幕上占据一个矩形区域，用户通过点击屏幕与之交互（点击、滑动、拖动等）；当然如何确定交互的位置、变化，如果不是View自己决定的变化，View又要如何响应外界因素的变化。
* View通过绘制自己与事件处理两种方式与用户交互。这就解决了如何交互的问题。
## Android系统中的View能做什么
一个用户界面，有许多View，既有基本View，也有复合View，Google的解决方案是，一套完整的用户界面用一个Window来表示，Window管理View的思路即是加载一个超级复合View——DecorView。它除了包含我们用户界面上那些View还包含了作为一个Window特有的View，---titlebar。这样Window中的View们被组织起来了，一个巨大的ViewGroup,这样ViewGroup下有若干ViewGroup和若干View，类似于数据结构中的树，叶子节点就是基本View。
#### 确定每个View的位置
我们在Activity中调用了setContentView(view),实际上就是讲用户界面的所有的Veiw交给DecorView中的一个FrameLayout,这个FrameLayout代表着可以分配给用户届满使用的屏幕区域。在inflat时，由ViewGroup读取layout_*之类的配置，然后生成一个ViewGroup特定的LayoutParams对象，再把这个对象存入子View中的，这样ViewGroup在为子View安排位置时，就可以参考这个LayoutParams中的信息了。我们发现在调用inflat时，除了布局文件中的id外，一般要求出入parent ViewGroup传入这个参数的目的就是为了读取layout配置信息。
#### 确定View的大小
这是一个开发者、View与ViewGroup三方相互商量的过程。
**第一步** 开发者在书写布局文件时，会为一个View写上layout_*配置，这是开发者向ViewGroup表达的，它的取值包括：
* 具体值：
* match_parent:表示将ViewGroup的所有屏幕区域都给这个View
* wrap_content:表示只要给这个View够展示他自己的空间就行，至于给多少，直接和View沟通。
**第二步**ViewGroup收到了开发者对View大小的说明，然后ViewGroup会综合考虑自己的空间大小以及开发者的请求，然后生成两个MeasureSpec对象（width与height）传给View，这个两个对象是ViewGroup向子View提出的要求，子View的宽高不能超过这两个参数的限制，子view可以从这两个参数中解释出宽高的Mode和Size。
Mode的取值有三种，它们代表了ViewGroup的总体态度：
1. EXACTLY:表示子View只能用size设置的指定的值，
2. AT_MOST:表示最多只能用Size指定的值，这表示使用者在设置一个View是Wrap_content时，View大小的决定权就交给View自己了，默认的View类中实现，比较粗暴，就是将此时ViewGroup提供的空间全占据。
3. UNSPECIFIED表示View自己看着办，把最理想的大小告诉ViewGroup,由它决定。
**第三步**，子View已经清楚地理解了ViewGroup和它的使用者对它的大小的期望和要求了。