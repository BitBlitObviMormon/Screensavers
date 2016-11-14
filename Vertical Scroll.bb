;a simple scrolling backdrop with parallax effect
; Verified 1.48 4/18/2001
;hit ESC to exit

If Trim$(Lower$(CommandLine$())) <> "/s" Then End

;go into graphics mode
window = CreateWindow("Screen Saver",0,0,GadgetWidth(Desktop()),GadgetHeight(Desktop()),0,0)
canvas = CreateCanvas(0,0,GadgetWidth(Desktop()),GadgetHeight(Desktop()),window)

;enable double buffering
SetBuffer CanvasBuffer(canvas)

HidePointer canvas

;load the backdrop images
backdrop1=LoadImage("graphics\stars1.bmp")
backdrop2=LoadImage("graphics\stars2.bmp")
backdrop3=LoadImage("graphics\stars3.bmp")
backdrop4=LoadImage("graphics\stars4.bmp")

;initialize scroll variable to 0
scroll_y#=0
mouse_x = MouseX()
mouse_y = MouseY()
;loop until ESC hit
While Not KeyDown(1)
	e = WaitEvent(10)
	If e = $101 Or e = $201 Or e = $204 Or mouse_x <> MouseX() Or mouse_y <> MouseY() Then End
	;draw the backdrop
	TileBlock backdrop1,0,Int(scroll_y#)
	
	;draw it again, but across a bit and moving 'faster'
	TileImage backdrop2,50,Int(scroll_y#*2)
	
	;and again!
	TileImage backdrop3,35,Int(scroll_y#*3)
	
	;and yet again!
	TileImage backdrop4,10,Int(scroll_y#*4)
		
	;scroll the backdrop
	scroll_y#=scroll_y#+0.25
	If scroll_y#=ImageHeight(backdrop1) Then scroll_y#=0
	
	;flip the front and back buffers
	FlipCanvas canvas
Wend

	