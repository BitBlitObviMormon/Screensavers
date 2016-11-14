;strange demo
;NO SHEEP THO' GEORGE ;)
;Copyright ©2000 EdzUp
;written by Ed Upton

If Trim$(CommandLine$()) = "/c" Or Trim$(CommandLine$()) = "" Then
	.prompt
	lines = Input("Number of Lines? (50-5000) ")
	If lines < 1 Then lines = 1
	If lines < 50 Then If Proceed("Are you sure? Less than 50 can be boring.") < 1 Then Goto prompt
	If lines > 5000 Then If Proceed("Are you sure? More than 5000 can make your computer slow!",True) < 1 Then Goto prompt
	If FileType(GetEnv$("APPDATA") + "\Jason's Screensavers") = 0 Then CreateDir(GetEnv$("APPDATA") + "\Jason's Screensavers")
	dir$ = GetEnv$("APPDATA") + "\Jason's Screensavers\"
	file = WriteFile(dir$ + "Weave.txt")
	WriteShort(file,lines)
	CloseFile(file)
	End
End If

If Trim$(CommandLine$()) <> "/S" Then End

SeedRnd(MilliSecs())

;adjust this for more lines
Global lns=BetweenTheLines() - 1

Global w = GadgetWidth(Desktop())
Global h = GadgetHeight(Desktop())
window = CreateWindow("Screen Saver",0,0,w,h,0,0)
canvas = CreateCanvas(0,0,w,h,window)
SetBuffer CanvasBuffer(canvas)

HidePointer canvas

Global r
Global g
Global b
Global tim

;X,Y screen coordinates
;F facing direction 
;T timer till it needs to change direction
Type bit
 Field x,y,f,t
End Type

tim=Rnd(500)
r=Rnd(255)
g=Rnd(255)
b=Rnd(255)

setup()
mouse_x = MouseX()
mouse_y = MouseY()
While Not KeyDown(1)
 e = WaitEvent(1)
 If e = $101 Or e = $201 Or e = $204 Or mouse_x <> MouseX() Or mouse_y <> MouseY() Then End
 update()
 tim=tim-1
 If tim<0
  tim=500
  r=Rnd(255)
  g=Rnd(255)
  b=Rnd(255)
 EndIf
FlipCanvas canvas
Wend
End

;setup lines
Function setup()
 For n=0 To lns
  bitz.bit=New bit
  bitz\x=Rnd(w)
  bitz\y=Rnd(h)
  bitz\f=Rnd(3)
  bitz\t=Rnd(20)
 Next
End Function

;update function
Function update()
 For bitz.bit=Each bit
  Color r,g,b
  Plot bitz\x,bitz\y
  bitz\t=bitz\t-1
  If bitz\t<=0
   bitz\f=Rnd(3)
   bitz\t=Rnd(20)
  EndIf
  If bitz\f=0 Then bitz\y=bitz\y-1
  If bitz\f=1 Then bitz\x=bitz\x+1
  If bitz\f=2 Then bitz\y=bitz\y+1
  If bitz\f=3 Then bitz\x=bitz\x-1
  If bitz\x<0 Then bitz\x=w-1
  If bitz\x>w-1 Then bitz\x=0
  If bitz\y<0 Then bitz\y=h-1
  If bitz\y>h-1 Then bitz\y=0
  Color 255,255,255
  Plot bitz\x,bitz\y
 Next
End Function

Function BetweenTheLines()
	If FileType(GetEnv$("APPDATA") + "\Jason's Screensavers") = 0 Then CreateDir(GetEnv$("APPDATA") + "\Jason's Screensavers")
	dir$ = GetEnv$("APPDATA") + "\Jason's Screensavers\"
	If FileType(dir$ + "Weave.txt") = 0 Then
		file = WriteFile(dir$ + "Weave.txt")
		WriteShort(file,500)
		CloseFile(file)
	End If
	file = ReadFile(dir$ + "Weave.txt")
	retval = ReadShort(file)
	CloseFile(file)
	Return retval
End Function