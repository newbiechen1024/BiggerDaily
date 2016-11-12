1、添加友盟分享

2、fragment中使用menu
当MainActivity中有menu的时候，fragment的menu就会作为MainActivity的一部分
当MainActivity中没有menu的时候，framgent的menu独自成一部分，其他fragment可切换

首先需要设置setHasOptionsMenu(true) 如果不设置，是fragment不会回调onCreateOptionMenu()
然后跟Activity一样
onCreateOptionMenu()
onOptionMenuSelected()

3、设置EditView为不可编辑状态

setFocus(false)
setFocusMode(false)

未完成

4、登陆绑定

首先创建全局的UserInfo类，并设置为Serialize，然后序列化存储

5、解决fragment卡顿的问题(未完成)
