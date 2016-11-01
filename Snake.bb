;Screensaver by Jason Hendricks

If Trim$(Lower$(CommandLine$())) <> "/s" Then End

Global FPS = 60	;The Framerate

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
x=w/2
y=0
Repeat
	vx=Rand(-20,20)
Until vx<>0
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
	SetBuffer(ImageBuffer(background))
	;Your code here...
	vy=vy+1	;Gravity
	If x < 0 Or x > w - 32 Then vx=-vx
	If y > h Then
		vy=-vy
		vy=vy + 1
	End If
	x=x+vx	;Move ball (x)
	y=y+vy	;Move ball (y)
	Color Rand(255),Rand(255),Rand(255)
	Oval x,y,32,32
	SetBuffer(CanvasBuffer(canvas))
	DrawImage(background,0,0) ;Draw the background
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