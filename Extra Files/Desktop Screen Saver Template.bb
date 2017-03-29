;Screensaver Template by Jason Hendricks

Global FPS = 30	;The Framerate

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
MoveMouse w/2, h/2
FlushEvents()					;Reset the event status
background = LoadDesktop()  	;Load the desktop image
FreeDesktop()					;Free the desktop grabber

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
	;Your Code Here:
	
	
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
	Return LoadImage(SystemProperty$("tempdir") + "Temp.bmp") ;Return the desktop
End Function

Function FreeDesktop()
	DeleteFile SystemProperty$("tempdir") + "Temp.bmp" ;Delete the temporary file
End Function
