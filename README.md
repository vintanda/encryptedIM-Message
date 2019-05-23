# encryptedIM-Message
### 基于netty的安全即时通讯系统
这是一个基于C/S架构的系统，我给它起名就叫Message，灵感来自Telegram，emmmm但是我真的太菜了，其实就是想试下netty罢辽.....
#### 客户端
采用MUI+H5plus进行开发，最终以app形式存在
#### 服务端
采用SpringBoot + Netty两个服务，SpringBoot的Controller只有用户的操作，其实可以重构一下的，实在是太着急辽。</br>
Netty进行通讯，可以指定通讯的协议，本系统支持http协议，感觉Netty开发上手有点无从下手，实现功能的时候又编码量好少，比NIO好写不知道多少倍，但是真的把Netty学号好难哦。</br>
聊天记录采用加密存储，本来通讯也要加密的...结果前端实在是不会写orz，我结构体都定义好辽....最后就写了一些工具类，感觉加密这块也蛮好玩的，打了一堆日志玩...
