# OpenR
Open source alternative for Total Commander, written in JavaFX. The main objective of this project is to create fully portable file manager. Currently OpenR has been tested on Linux Ubuntu 16.10 and Windows 10.

## Features
### Overview
<img src="http://oi64.tinypic.com/292vbeq.jpg" width="400"><br />
The main application window is divided into two parts. As seen on screenshot above, there are two working directories. You can navigate over directories and change curent working directory by double-clicking desired directory or by entering absolute path in text field above the directory view. Directory marked as ".." is parent directory, and - if it's possible - you can go up in your file system. OpenR disallows user for going up, if the curent directory is root and has no parent.
### Context menu
<img src="http://i66.tinypic.com/2n83hp0.png"><br/>
Any position on the list, except parent directory, can be right-clicked. There are some options available. Selecting **Open** option when directory is selectes, causes OpenR to open this directory. This is an alternative way to navigate over file system. Using the same option on file results in starting default OS program supporting opening selected file. For example, double click on **txt** starts default OS program for opening text files, on Windows it could be Notepad, on Linux - Vim, Nano or Gedit. This option works only if OS supports Java desktop feature. User can also delete selected item - permanently or move it to the system trash. Curently operation is supported only on Windows and Linux. Moving to trash shows no warning message, but selecting **Delete permanently** shows confirmation dialog. The file is deleted permanently only if user confirms deletion. However, file or directory can be restored using special software, like PhotoRec or TestDisk.
Context menu has more options - for creating file, directory or document.

OpenR can create:
- empty file
- text file
- XML file
- HTML file

...and following documents:
- DOCX document
- PPTX slideshow

Creating file, directory or document affects curent working directory. For example - if user right-clicks on the left panel, the file (or directory) will be created in directory displayed in mentioned panel. User can rename file or directory. Renaming file **does not affect file extension**. For example - user wants to rename file _index.php_. As the new name user enters _index.html_. The result won't be _index.html_, but _index.html.php_. In the future user will have ability of choosing to keep the extension or not. Context menu has additional positions - to cut, copy and paste selected file or directory. Cutting file causes it to delete after paste. After selecting **Paste** option clipboard is cleared. The last option available in context menu is **Properties**.
#### Properties
<img src="http://i65.tinypic.com/i6jvbo.png" width="200"><img src="http://i68.tinypic.com/241lgnk.png" width="200"> <br />
Properties option provides basic information about selected item. Determining type of item is OS dependent, so sometimes information about selected item can be misleading.
#### Context menu when nothing selected
If user clicks on the panel but no item is selected, there will be shown short context menu. User can create file, document or directory in the same way as by the selecting item in the directory view.

### Toolbar
<img src="http://i63.tinypic.com/2pzl4d1.png"><br />
Toolbar is located above central panel. There are four icons. Starting from the left - **Create new file**, **Grep**, **Find file** and **Exit**. The last option closes OpenR.
#### Create new file
<img src="http://i65.tinypic.com/2qansdi.png" width="200"> </br>
User can specify file name, desired path and type. Available paths are related to those visible in the main window.
#### Grep
<img src="http://i65.tinypic.com/dlqbsx.png" width="200"> <br/>
Grep provides grep functionality to OpenR. User can specify to grep recursively or not. [Grep - Wikipedia](https://en.wikipedia.org/wiki/Grep)
#### Find file
<img src="http://i67.tinypic.com/i44ffd.png" width="200"> <br/>
User can find file or directory by name. As same as in the grep window, user can select **Recursive** option. Double-click on found file or directory causes OpenR to open it using default system program. If user selects directory, default window manager is used, for example Explorer on Windows or Nautilus on Ubuntu. 

### Menu bar
Menu bar contains only few options and it is not nesessary to describe it in detail. User can hide status bar displayed on the bottom of main window. There is also help menu with two options - **Help** and **About**. The first contains user guide, the second - basic informations about application and author.

## Known bugs
- switching selection between left and right panel clears _Selected file_ text field
- loading directory can freeze application for a while, especially if there are lots of files and directories in loaded directory
- **Help** window is still WIP, help files are curently empty
- _Back_ and _Forward_ buttons in **Help** window does not work properly
