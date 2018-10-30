# 个人项目3
# 数据存储（一）应用开发

## 第九周任务  
## 数据存储（一）
---

### 实验目的
1. 学习SharedPreference的基本使用。  
2. 学习Android中常见的文件操作方法。
3. 复习Android界面编程。
   
---

### 实验内容

#### 要求  
* Figure 1：首次进入，呈现创建密码界面。  
 ![preview](https://gitee.com/code_sysu/PersonalProject3/blob/master/manual/images/fig%201.jpg)
* Figure 2：若密码不匹配，弹出Toast提示。  
 ![preview](https://gitee.com/code_sysu/PersonalProject3/blob/master/manual/images/fig%202.jpg) 
* Figure 3：若密码为空，弹出Toast提示。  
 ![preview](https://gitee.com/code_sysu/PersonalProject3/blob/master/manual/images/fig%203.jpg) 
* Figure 4：退出后第二次进入呈现输入密码界面。  
 ![preview](https://gitee.com/code_sysu/PersonalProject3/blob/master/manual/images/fig%204.jpg) 
* Figure 5：若密码不正确，弹出Toast提示。  
 ![preview](https://gitee.com/code_sysu/PersonalProject3/blob/master/manual/images/fig%205.jpg)
* Figure 6：文件加载失败，弹出Toast提示。  
 ![preview](https://gitee.com/code_sysu/PersonalProject3/blob/master/manual/images/fig%206.jpg) 
* Figure 7：成功导入文件，弹出Toast提示。  
 ![preview](https://gitee.com/code_sysu/PersonalProject3/blob/master/manual/images/fig%207.jpg) 
* Figure 8：成功保存文件，弹出Toast提示。  
 ![preview](https://gitee.com/code_sysu/PersonalProject3/blob/master/manual/images/fig%208.jpg) 
###
1.  如Figure 1至Figure 8所示，本次实验演示应用包含两个Activity。 
2.  首先是密码输入Activity：
    * 若应用首次启动，则界面呈现出两个输入框，分别为新密码输入框和确认密码输入框。  
    * 输入框下方有两个按钮：  
        - OK按钮点击后：  
            + 若New Password为空，则发出Toast提示。见Figure 3。
            + 若New Password与Confirm Password不匹配，则发出Toast提示，见Figure 2。
            + 若两密码匹配，则保存此密码，并进入文件编辑Activity。
        - CLEAR按钮点击后：清楚两输入框的内容。  
    * 完成创建密码后，退出应用再进入应用，则只呈现一个密码输入框，见Figure 4。
        - 点击OK按钮后，若输入的密码与之前的密码不匹配，则弹出Toast提示，见Figure 5。
        - 点击CLEAR按钮后，清除密码输入框的内容。
    * **出于演示和学习的目的，本次实验我们使用SharedPreferences来保存密码。但实际应用中不会使用这种方式来存储敏感信息，而是采用更安全的机制。见[这里](http://stackoverflow.com/questions/1925486/android-storing-username-and-password)和[这里](http://stackoverflow.com/questions/785973/what-is-the-most-appropriate-way-to-store-user-settings-in-android-application/786588)。**
3.  文件编辑Activity：
    * 界面底部有三个按钮，高度一致，顶对齐，按钮水平均匀分布，三个按钮上方除ActionBar和StatusBar之外的全部空间由一个EditText占据（保留margin）。EditText内的文字竖直方向置顶，左对齐。
    * 在编辑区域输入任意内容，点击SAVE按钮后能保存到指定文件（文件名随意）。成功保存后，弹出Toast提示，见Figure 8。
    * 点击CLEAR按钮，能清空编辑区域的内容。
    * 点击LOAD按钮，能够从同一文件导入内容，并显示到编辑框中。若成功导入，则弹出Toast提示。见Figure 7.若读取文件过程中出现异常（如文件不存在），则弹出Toast提示。见Figure 6.
4.  特殊要求：进入文件编辑Activity后，若点击返回按钮，则直接返回Home界面，不再返回密码输入Activity。


 

---

### 验收内容
1. 布局显示与Figure 1 至 Figure 8一致。
2. 应用逻辑与上文描述一致。
	* 异常情况弹出Toast提示。
	* 创建密码后重新启动应用，直接显示单个输入框输入密码。
	* 文本编辑页面：能够正常保存和读取文件。
3. 在实验报告中简要描述Internal Storage和External Storage的区别，以及它们的适用场景。
4. 代码+实验报告（先在实验课上检查，检查后再pr）

---

### 完成期限
第十一周各班实验课进行检查，未通过者需在下一周进行修改与重新检查，如再次未通过则扣除这一周任务的分数。

---


### 个人项目提交方式
经码云张总监技术分享，在同学们对git基本操作掌握后，现确定作业提交步骤如下：

1. 布置的个人项目先fork到个人仓库下；
2. clone自己仓库的个人项目到本地目录；
3. 在个人项目中，在code、report目录下，进入自己所在的班别，然后新建个人目录，目录名为“学号+姓名拼音”，例如“**12345678WangXiaoMing**”；
4. 在“code\班别\12345678WangXiaoMing”目录下，新建Android项目，按要求完成界面设计，代码编写等，注意.gitignore的编写，避免添加不必要的中间文件、临时文件到git中，如果在检查时发现提交了不必要的文件，会扣一定的分数；
5. 实验报告按给出的模版(manual中的report_template.md)的内容要求，以md的格式，写在“report\班别\12345678WangXiaoMing”目录下，结果截图也放在该目录下；
6. 完成任务需求后，Pull Request回主项目的master分支，PR标题为“班级+学号+姓名”， 如“**周三班12345678王小明**”；
7. 一定要在deadline前PR。因为批改后，PR将合并到主项目，所有同学都能看到合并的结果，所以此时是不允许再PR提交作业的。