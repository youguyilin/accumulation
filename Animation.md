## Android 动画机制
在Android 3.0之前的版本，我们使用的动画类型有两种，分别为逐帧动画和部件动画；Android 3.0之后，增添 属性动画；同时在Android 4.4中，Android SDK又为开发者带来了android.transition框架，使得开发者可以通过一种更直观的方式定义动画效果；以下分别介绍3着四种动画类型：
#### 逐帧动画（Frame Animation）
逐帧动画也叫Drawalbe Animation,是最简单最直观的动画类型，他利用人眼的视觉暂留效应——也就是光对视网膜所产生的视觉在光停止作用后，仍会保留一段时间的现象
	在Android中实现逐帧动画，由设计师给出一系列状态不断变化的图片，开发者指定动画中每一帧对应的图片和持续的视觉，然后就可以开始播放动画 了。具体而言，有两种方式可定义逐帧动画，采用XML资源文件和代码实现；
即在anim目录中新建动画xml，使用<animation-list>定义动画帧序列，使用<item>标签来定义动画的每一帧，并指定帧的持续时间等属性
* 代码方式
```	
	AnimationDrawable animDrawable = new AnimationDrawable();
	for(int i = 1;i < 5; i++){
		int id = getResources().getIdentifier("common_loading_" +  i, "drawable",getPackageName());
		Drawable drawable = getResources().getDrawable(id);
		animDrawable.addFrame(drawable,120);
	
	}
	imageView.setBackgroundDrawable(animDrawable);
	animDrawable.setOneShot(flase);
	
```
在适当的时刻可以通过获取AnimationDrawable对象实例，用来控制动画的播放和停止
#### 补间动画（Tween Animation）
补间动画是指开发者无需定义动画过程中的每一帧，秩序要定义动画的开始和结束这两个关键帧，并制定动画变化的时间和方式等，然后由Anroid系统进行计算，通过在两个关键帧之间插入渐变值来实现平滑过渡，从而对View的内容完成一系列的图形变换来实现的动画效果，主要包括四种基本的效果：Alpha、Scale、Translate、Rotate;也可以组合使用；同样补间动画也分为XML资源文件和代码两种方式；首先介绍一下插值器Interpolator
##### 插值器 Interpolator
Interpolator会依据类型的不同，选择不同的算法计算出在补间动画期间所需动态插入帧的密度和位置，Interpolator负责动画的变化速度，使得前面所说的四种基本动画效果能够以匀速、加速、减速、抛物线等多种速度进行变化。
在Android代码中，Interpolator类其实是一个空接口，它继承自TimeInterpolator，TimeInterpolator时间插值器游戏动画进行非线性运动变换，如加速和减速等，该接口中只有 float getInterpolation（float input）这个方法。入参是一个0.0 ~ 1.0的值，返回值也可以小于0.0，也可以大于1.0
>自定义补间动画，只需要继承Animation，并重写这个抽象基类中的applyTransformation方法，
```
public class MyAnimation extends Animation{

	@Override
	protected void applyTransformation(float interpolatedTime,Transformation transformation){
	super.applyTransformation(interpolatedTime,transformation);
}
}
```
其中，iterpolatedTime表示动画的时间进行比，无论动画的实际持续时间是多少，这个参数总是会从0变化到1，transformation表示动画在不同时刻对View的变形程度。
#### 属性动画（Property Animation）
在补间动画中，我们只能改变View的绘制效果，View的真是属性是没有变化的，而属性动画则可以直接改变View对象的属性值，同时属性动画几乎可以对任何对象执行动画，而不是局限在View对象上，从某种意义上讲，Property Animation可以说是增强版的补间动画，属性动画的基类是Animator，他是一个抽象类，所以不会直接使用这个类，通常都是继承并重写其中的相关方法，Android SDK 为开发者提供了几个子类，基本上可以满足任务需求。同样我们先介绍一个重要的概念Evaluator
#####  Evaluate
它是用来控制属性动画如何计算属性值的，它的接口定义是TypeEvaluator，其中定义了evaluate方法，供不同类型的子类实现。
```
public interface TypeEvaluator<T>{
	public T evaluate(floate fraction, T startValue, T endValue);
}
```
常见的实现类有IntEvaluator,FlaitEvaluator、ArgbEvaluator等。其中ArgbEvaluator主要就通过一个初始值、一个结束值及一个进度比，计算出每个进度对应的ARGB值。
* AnimatorSet
 AnimatorSet也是Animator的子类，用来组合多个Animator，并指定这些Animator是顺序播放，还是同时播放
* ValueAnimator
  ValueAnimator是属性动画最重要的一个类，继承自Animator。它定义了属性动画大部分的核心功能，包括计算各个帧的属性值、处理更新事件、按照属性值得烈性控制计算规则等。一个完整的属性动画由两部分组成：
 1. 计算动画各个帧的相关属性
 2. 将这些属性值设置给指定的对象
ValueAnimator为开发者实现第一部分的功能，第二部分功能由开发者自行设置。ValueAnimator的构造函数是空实现，一般都是使用静态工程的方法来进行实例化。获取到实例之后，接着需要设置动画持续时间、插值方式、重复次数等属性值，然后启动动画，最后需要为ValueAnimator注册AnimatorUpdateListener监听器，并在监听器的onAnimationUpdate方法中将计算出来的属性值设置给指定的对象。
##### ObjectAnimator
ObjectAnimator是ValueAnimator的子类，封装了上面所说的第二部分的功能，所以在实际开发中使用的更多的就是ObjectAnimator，只有在ObjectAnimator实现不了的场景下，才考虑使用ValueAnimator，二者最大的不同是需要指定动画作用的具体对象和对象的属性名，而且ObjectAnimator一般不需要祖册AnimatorUpdateListener监听器。
使用ObjectAnimator有一下几点需要注意：
1. 需要为对象对应的属性体统setter方法。
2. 如果动画的对象是View，那么为了能显示动画效果，在某些情况下，可能还需要注册AnimatorUpdateListener监听器，并在其毁掉方法onAnimatorUpadte中调用View的invalidate方法来刷新View的显示。
当然，属性动画也是可以在XML中定义，在工程res/animator目录中存放的就是属性动画XML文件。
#### 过渡动画（Transition Animation）
过度动画实在Android 4.4引入的新的动画框架，它本质上还是属性动画，只不过对属性动画做了一层封装，方便Activity或者View的过渡动画效果。和属性动画相比，过渡动画最大的不同时需要为动画前后准备不同的布局，不通过对应得API实现两个布局的过渡动画，而素心广东话只需要一个布局文件。
首先了解一下Transition Animation框架的几个基本概念：
* Scene:定义了页面的当前状态信息，
```
public static Scene getSceneForLayout(ViewGroup sceneRoot,int layoutId,Context context){
}
```
* Transition：定义了界面之间切换的动画信息，在使用TransitionManager是没有指定使用哪个Transition,那么会使用默认的AutoTranstion——先隐藏（变透明），然后移动指定的对象，最后显示出来。
* TransitionManager:控制Scene之间切换的控制器，