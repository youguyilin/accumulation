## 滑动效果：实现View根据用户的滑动时间监听，动态的改变View的坐标，从而实现View跟随用户触摸的滑动而滑动。
1. #### Android 中View的位置和点击位置的获取
* View提供的获取坐标的方法：
	* getTop():获取到的是View自身的顶边到其父布局的顶边的距离
	* getLeft():获取到的是View的左边到其父布局的左边的距离
	* getRight():获取到的是View右边到其父布局的左边的距离
	* getBottom():获取到的是View的底边到其父布局顶边的距离
* MotionEvent提供的方法：
	* getX():获取点击事件距离View自身左边的距离，即视图坐标系
	* getY():获取点击事件距离View顶边的距离，即视图坐标系
	* getRawX():获取点击事件距离整个屏幕左边的距离，即绝对坐标
	* getRawY():获取点击事件距离整个屏幕顶边的距离，即绝对坐标系。
2.  #### 实现滑动的七种方式
无论哪一种实现方式，它们的实现思想基本是一致的：**当触摸View时，系统记下当前触摸点坐标，当手指移动时，系统记下移动后的触摸点坐标，从而获取到相对于前一次坐标点的偏移量，并通过偏移量来修改View的坐标，不断重复，实现滑动过程**
* #### layout()方法
	通过layout(getLeft() + offsetX,getTop() + offsetY,getRight() + offsetX,getBottom() + offsetY);
在获取偏移量的时候既可以使用视图坐标系，也可以使用绝对坐标系，但是在使用绝对坐标系的时候，每次执行完ACTION_MOVE的逻辑后，需要初始化坐标，这样才能正确的获取偏移量。
* #### offsetLeftAndRight()与offsetTopAndBottom()
	这两个方法是系统提供的一个队左右、上下移动的API的封装。在使用完同样的计算偏移量逻辑之后，便可调用offsetTopAndBottom(offsetX);和offsetLeftAndRight(offsetY)实现偏移滑动。
* #### LayoutParams
	LayoutParams保存了一个View的布局参数。因此在程序中，通过改变LayoutParans来动态修改一个布局参数的位置参数，从而达到改变View位置的效果。我们可以通过getLayoutParams来获取LayoutParams对象，但需要根据View所在View父布局的类型来设置不同的类型，所以前提就是必须要有一个父布局，不然父布局无法获取LayoutParams。在通过改变LayoutParams来改变一个View的位置时，通常改变得是这个View的Margin属性，在除了使用LayoutParams之外，还可以使用ViewGroup.MarginLayoutParams来实现。不需要考虑父布局的类型，本质是一样的。
* #### scrollTo与srollBy
	如果我们在获取偏移量之后直接使用scrollBy或scrollTo会发现View并没有移动，它们移动的是View的content,让View的内容移动，如果在ViewGroup中使用，移动的将是所有子View，所以我们可以通过代码：
((View)getParent()).scrollBy(offsetX,offsetY);但是这样会发现View的移动为乱动，需要将offset设为负值才能实现跟随手指移动的效果。
* #### Scroller
前面实现的跟随滑动的效果都是利用人眼的视觉暂留特性。
使用scroller,需要：
	1. 初始化Scroller
		mScroller = new Scroller();
	2. 重写computerScroller方法实现模拟滑动
		系统在绘制View的时候会在draw（）方法中调用该方法：


	@Override
	public void computeScroll(){
		super.computeScroll();
		//判断Scroller是否执行完毕
		if(mScroller.computeScrollOffset()){
			((View)getParent()).scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
			//通过重绘来不断嗲用computeScroll
			invalidate();
		}
	}
