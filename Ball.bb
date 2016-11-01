;Screensaver by Jason Hendricks

Global FPS = 60	;The Framerate

If Trim$(Lower$(CommandLine$())) <> "/s" Then End

;For GrabDesktop()
w=GadgetWidth(Desktop())		;The desktop width
h=GadgetHeight(Desktop())		;The desktop height
GrabDesktop() 					;Grab the desktop
window = CreateWindow("Blitz Screensaver",0,0,w,h,0,0)
canvas = CreateCanvas(0,0,w,h,window)
SetBuffer(CanvasBuffer(canvas))	;Set the buffer to the back buffer
timer = CreateTimer(FPS)		;Create the timer
HidePointer(canvas)				;Hide the mouse pointer
FlushEvents()					;Reset the event status
background = LoadDesktop()  	;Load the desktop image
FreeDesktop()					;Free the desktop grabber

;Loading Content...
ball = CreateImage(32,32) ;Create ball image
SetBuffer(ImageBuffer(ball)) ;Begin drawing on the image
Color 255,0,0 ;Set color to red
Oval 0,0,32,32,True ;Draw circle
SetBuffer(CanvasBuffer(canvas)) ;Set the buffer back to the back buffer
x=w/2
y=0
Repeat
	vx=Rand(-20,20)
Until vx <> 0
vy=0

;MAIN LOOP
Repeat
	If KeyDown(1) Then Exit ;For now, if the user presses escape then end the screen saver
	Select WaitEvent()
		Case $101	;KEYDOWN EVENT
			End ;End the program
		Case $201	;MOUSEDOWN EVENT
			End ;End the program
		Case $203	;MOUSEMOVE EVENT
			End ;End the program
		Case $204	;MOUSEWHEEL EVENT 
			End ;End the program
		Case $4001	;TIMER EVENT
			;Continue the loop
	End Select
	Cls
	DrawImage(background,0,0) ;Draw the background
	If y>h Then
		vy=-vy - 1
	End If
	vy=vy+1
	If x+vx>w-32 Or x+vx<0 Then vx=-vx
	y=y+vy
	x=x+vx
	DrawImage(ball,x,y)
	FlipCanvas canvas
Forever

;Returns an image of the desktop
Function GrabDesktop()
	;Create an image to copy the desktop to
	image = CreateImage(GadgetWidth(Desktop()),GadgetHeight(Desktop()),2)
	;Copy the desktop to the image
	CopyRect 0,0,GadgetWidth(Desktop()),GadgetHeight(Desktop()),0,0,DesktopBuffer(),ImageBuffer(image)
	;Save the image for loading
	SaveImage(image,SystemProperty$("tempdir") + "Temp.bmp")
End Function

Function LoadDesktop()
	image = LoadImage(SystemProperty$("tempdir") + "Temp.bmp") ;Return the desktop
	Return image
End Function

Function FreeDesktop()
	DeleteFile SystemProperty$("tempdir") + "Temp.bmp" ;Delete the temporary file
End Function