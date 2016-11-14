;simple graphics example
; Verified 1.48 4/18/2001

If Trim$(CommandLine$()) = "/c" Or Trim$(CommandLine$()) = "" Then
	.prompt
	Repeat
		lines = Input("Rectangles (0), Ellipses (1), or Random(2)? ")
	Until lines >= 0 And lines <= 2
	If FileType(GetEnv$("APPDATA") + "\Jason's Screensavers") = 0 Then CreateDir(GetEnv$("APPDATA") + "\Jason's Screensavers")
	dir$ = GetEnv$("APPDATA") + "\Jason's Screensavers\"
	file = WriteFile(dir$ + "Shapes.txt")
	WriteShort(file,lines)
	CloseFile(file)
	End
End If

If Trim$(Lower$(CommandLine$())) <> "/s" Then End

Const FPS = 60
shape = BetweenTheLines()
random = False
If shape = 2 Then random = True

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

			If random = True Then shape = Rand(0, 1)

			;draw a random rectangle
			If shape = 0 Then
				Rect Rand(w),Rand(h),Rand(b),Rand(b)
			Else shape = 1
				Oval Rand(w),Rand(h),Rand(b),Rand(b), True
			End If
			FlipCanvas canvas
		EndIf
	EndIf
Wend

Function BetweenTheLines()
	If FileType(GetEnv$("APPDATA") + "\Jason's Screensavers") = 0 Then CreateDir(GetEnv$("APPDATA") + "\Jason's Screensavers")
	dir$ = GetEnv$("APPDATA") + "\Jason's Screensavers\"
	If FileType(dir$ + "Shapes.txt") = 0 Then
		file = WriteFile(dir$ + "Shapes.txt")
		WriteShort(file,0)
		CloseFile(file)
	End If
	file = ReadFile(dir$ + "Shapes.txt")
	retval = ReadShort(file)
	CloseFile(file)
	Return retval
End Function