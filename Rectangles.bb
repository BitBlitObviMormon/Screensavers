;simple graphics example
; Verified 1.48 4/18/2001

If Trim$(Lower$(CommandLine$())) <> "/s" Then End

Const FPS = 60

;go into graphics mode
w=GadgetWidth(Desktop())
h=GadgetHeight(Desktop())
window = CreateWindow("Blitz Screensaver",0,0,w,h,0,0)
canvas = CreateCanvas(0,0,w,h,window)
SetBuffer(CanvasBuffer(canvas))	;Set the buffer to the back buffer
timer = CreateTimer(FPS)		;Create the timer
HidePointer(canvas)				;Hide the mouse pointer
b=Int(w/20)

mouse_x = MouseX()
mouse_y = MouseY()
SetBuffer(CanvasBuffer(canvas))
;keep looping until ESC hit
While Not KeyDown(1)
	e = WaitEvent(1)
	
	If e = $101 Or e = $201 Or e = $204 Or mouse_x <> MouseX() Or mouse_y <> MouseY() Then
		End
	Else If e = $4001
		If EventSource() = timer Then
			;set a random color
			Color Rand(255),Rand(255),Rand(255)
			
			;draw a random rectangle
			Rect Rand(w),Rand(h),Rand(b),Rand(b)
			FlipCanvas canvas
		EndIf
	EndIf
Wend