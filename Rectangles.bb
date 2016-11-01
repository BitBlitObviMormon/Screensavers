;simple graphics example
; Verified 1.48 4/18/2001
;
;hit ESC to exit

If Trim$(Lower$(CommandLine$())) <> "/s" Then End

;go into graphics mode
w=GadgetWidth(Desktop())
h=GadgetHeight(Desktop())
b=Int(w/20)
Graphics w,h,0,1

mouse_x = MouseX()
mouse_y = MouseY()
SetBuffer(BackBuffer())
;keep looping until ESC hit
While Not KeyDown(1)
	e = WaitEvent(1)
	
	If e = $101 Or e = $201 Or e = $204 Or mouse_x <> MouseX() Or mouse_y <> MouseY() Then End
	
	;set a random color
	Color Rnd(255),Rnd(255),Rnd(255)
	
	;draw a random rectangle
	Rect Rnd(w),Rand(h),Rand(b),Rand(b)
	Flip
	Delay 1
Wend