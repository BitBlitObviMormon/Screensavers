; ***********************
; *MORPHING DOTS        *
; ***********************
; *CODING BY TRACER     *
; *TRACERBB@HOTMAIL.COM *
; *verified 1.48 4/18/01*
; ***********************

Global numdots = 5000										; Number of Dots
Global numobjs = 18											; Number of Objects.
Global distance  = 400										; Needed for perspective.
Global vx#													; X location.
Global vy#													; Y location.
Global vz# 													; Z location.
Global obj = 1												; Start Object.
Global r = 2												; The radius of the dots (in pixels)
Global FPS = 200											; The Frames Per Second (Keep it between 50 and 200)
Global width = GadgetWidth(Desktop())
Global height = GadgetHeight(Desktop())

ReadData()

If Trim$(Lower$(CommandLine$())) = "" Or Trim$(Lower$(CommandLine$())) = "/c" Then
	configuring = True ;Let the program know that we are configuring!
	;The preview window
	configpreview = CreateWindow("Morphdot Preview",GadgetWidth(Desktop())/2-320,GadgetHeight(Desktop())/2-240,640,480,0,1)
	;The configuration window
	window = CreateWindow("Morphdot Configuration",GadgetWidth(Desktop())/2,GadgetHeight(Desktop())/2,300,126,0,17)
	SetGadgetShape(window,GadgetWidth(Desktop())/2,GadgetHeight(Desktop())/2,300+(300-ClientWidth(window)),126+(126-ClientHeight(window))) ;Readjust the window so that the client width and height is 300,126
	SetGadgetShape(configpreview,GadgetWidth(Desktop())/2-320,GadgetHeight(Desktop())/2-240,640+(640-ClientWidth(configpreview)),480+(480-ClientHeight(configpreview))) ;Readjust the window so that the client width and height is 640,480
	canvas = CreateCanvas(0,0,640,480,configpreview) ;Create the Canvas
	width = 640
	height = 480
	label1 = CreateLabel("Number of Dots",0,0,300,12,window)
	label2 = CreateLabel("Dot Radius",0,32,300,12,window)
	label3 = CreateLabel("Speed",0,64,300,12,window)
	slider1 = CreateSlider(0,16,300,16,window,1)
	slider2 = CreateSlider(0,48,300,16,window,1)
	slider3 = CreateSlider(0,80,300,16,window,1)
	button = CreateButton("OK",100,100,100,24,window,4)
	SetSliderRange(slider1,0,9901)
	SetSliderRange(slider2,0,5)
	SetSliderRange(slider3,0,221)
	SetBuffer(CanvasBuffer(canvas))
Else If Trim$(Lower$(CommandLine$())) = "/s" Then
	window = CreateWindow("Screen Saver",0,0,width,height,0,0)
	canvas = CreateCanvas(0,0,width,height,window)
	SetBuffer(CanvasBuffer(canvas))
	HidePointer canvas
Else
	End ;Exit the program
End If

Global timer = CreateTimer(FPS)								; The timer

If configuring = True Then
	SetSliderValue(slider1,numdots - 100)
	SetSliderValue(slider2,r - 1)
	SetSliderValue(slider3,FPS - 30)
	SetGadgetText(label1,"Number of Dots: " + numdots)
	SetGadgetText(label2,"Dot Radius: " + r)
	SetGadgetText(label3,"Speed: " + FPS)
End If

.reset														; Resets the array

; Arrays used by 3d code.
Dim points(numdots, 3)										; Contains the next Object to morph to.
Dim tpoint(numdots, 3)										; Contains the current (being morphed) Object.

Objects(obj)												; Get first Object.
; Put first Objects into displayed Objects array.
For t = 1 To numdots	
	tpoint(t,1) = points(t,1)
	tpoint(t,2) = points(t,2)
	tpoint(t,3) = points(t,3)
Next
FlushEvents
; Loop until esc is pressed.
While Not KeyDown(1)
	Color 255,255,255
	
	If cnt > 999
		obj = obj + 1
		If obj > numobjs Then obj = 1
		Objects(obj)
		cnt = 0
	EndIf
	morphing()
	cnt = cnt + 1
	threed()												; Call the threed thing.
	FlipCanvas canvas,False
	Repeat
		Select WaitEvent()
			Case $101										;KEY DOWN EVENT
				If configuring = False Then End
			Case $201										;MOUSE DOWN EVENT
				If configuring = False Then End
			Case $203										;MOUSE MOVE EVENT
				If configuring = False Then End
			Case $204										;MOUSE WHEEL EVENT
				If configuring = False Then End
			Case $4001										;TIMER EVENT
				If EventSource() = timer Then Exit			;Exit the loop
			Case $401										;GADGET ACTION EVENT
				;Set the values of the sliders
				e=EventSource()
				r = SliderValue(slider2) + 1
				FPS = SliderValue(slider3) + 30
				SetGadgetText(label2,"Dot Radius: " + r)
				SetGadgetText(label3,"Speed: " + FPS)
				If e = slider3 Then
					FreeTimer(timer)
					timer = CreateTimer(FPS)
				End If
				If e = slider1 Then
					numdots = SliderValue(slider1) + 100
					SetGadgetText(label1,"Number of Dots: " + numdots)
					Goto reset
				End If
				If e = button Then
					SaveData()								;Save the data
					End										;Close the application
				End If
			Case $803										;CLOSE EVENT
				End											;Close the application
		End Select
	Forever
	Cls														; Clear the screen.
Wend

Function threed()
	vx# = vx# + 0.5											; X rotation speed of ball.
	vy# = vy# + 0.5											; Y rotation speed of ball.
	vz# = vz# + 0.5											; Z rotation speed of ball.

	For n = 1 To numdots
		x3d = tpoint(n, 1)
		y3d = tpoint(n, 2)
		z3d = tpoint(n, 3)
       
		ty# = ((y3d * Cos(vx#)) - (z3d * Sin(vx#)))
		tz# = ((y3d * Sin(vx#)) + (z3d * Cos(vx#)))
		tx# = ((x3d * Cos(vy#)) - (tz# * Sin(vy#)))
		tz# = ((x3d * Sin(vy#)) + (tz# * Cos(vy#)))
		ox# = tx#
		tx# = ((tx# * Cos(vz#)) - (ty# * Sin(vz#)))
		ty# = ((ox# * Sin(vz#)) + (ty# * Cos(vz#)))
		nx  = Int(0.8 * width * (tx#) / (distance - (tz#))) + (.5 * width)
		ny  = Int((.375 * width) - (0.8 * width * ty#) / (distance - (tz#)))
      
		setcolor(tz#)
		Oval nx,ny,r,r
;		Plot nx,ny
	Next
End Function

; This function looks at the z-value of the pixel
; and sets the color accoordingly.
Function setcolor(t#)
	If t# <= 200 And t# >= 75
		Color 255,255,255
	Else If t# <= -155 Then
		Color 25,25,25
	Else
		Color Int(t#)+180,Int(t#)+180,Int(t#)+180
	EndIf
End Function

Function Objects(obj)
	Select obj
		Case 1
			; line
			For t = 1 To numdots
				points(t,1) = Rnd(-50,50)
				points(t,2) = 0
				points(t,3) = 0
			Next
		Case 2
			; sphere
			For t= 1 To numdots
				xd = Rnd(-90,90)
				x0 = (Cos(xd)* 10) * (Cos(t*360.0/numdots) * 10)
				y0 = (Cos(xd) * 10) * (Sin(t*360.0/numdots) * 10)
				z0 = Sin(xd) * 100
				points(t,1) = x0
				points(t,2) = y0
				points(t,3) = z0
			Next
		Case 3
			; cone thing
			For t= 1 To numdots
				xd = Rnd(-90,90)
				x0 = (Cos(xd)* 10) * (Cos(t*360.0/numdots) * 10)
				y0 = (Cos(xd) * 10) * (Sin(t*360.0/numdots) * 10)
				z0 = Sin(t*360.0/numdots) * 100
				points(t,1) = x0
				points(t,2) = y0
				points(t,3) = z0
			Next
		Case 4
			; twist
			For t= 1 To numdots
				xd = Rnd(-90,90)
				x0 = (Cos(xd)* 10) * (Cos(xd) * 10)
				y0 = (Cos(xd) * 10) * (Sin(xd) * 10)
				z0 = Sin(xd) * 100
				points(t,1) = x0
				points(t,2) = y0
				points(t,3) = z0
			Next
		Case 5
			; cylinder
			For t= 1 To numdots
				xd = Rnd(-90,90)
				x0 = (Cos(xd)* 10) * (Cos(xd) * 10)
				y0 = (Cos(xd) * 10) * (Sin(xd) * 10)
				z0 = Sin(t*360.0/numdots) * 100
				points(t,1) = x0
				points(t,2) = y0
				points(t,3) = z0
			Next
		Case 6
			; hat kinda thing
			For t= 1 To numdots
				xd = Rnd(-90,90)
				x0 = (Cos(xd)* 10) * (Cos(xd) * 10)
				y0 = (Cos(t*360.0/numdots) * 10) * (Sin(xd) * 10)
				z0 = Sin(t*360.0/numdots) * 100
				points(t,1) = x0
				points(t,2) = y0
				points(t,3) = z0
			Next
		Case 7
			; bend plain kinda thing
			For t= 1 To numdots
				xd = Rnd(-90,90)
				x0 = (Cos(t*360.0/numdots)* 10) * (Cos(t*360.0/numdots) * 10)
				y0 = (Cos(t*360.0/numdots) * 10) * (Sin(xd) * 10)
				z0 = Sin(t*360.0/numdots) * 100
				points(t,1) = x0
				points(t,2) = y0
				points(t,3) = z0
			Next
		Case 8
			; curl
			For t= 1 To numdots
				xd = Rnd(-90,90)
				x0 = (Cos(t*360.0/numdots)* 10) * (Cos(t*360.0/numdots) * 10)
				y0 = (Cos(t*360.0/numdots) * 10) * (Sin(t*360.0/numdots) * 10)
				z0 = Sin(t*360.0/numdots) * 100
				points(t,1) = x0
				points(t,2) = y0
				points(t,3) = z0
			Next
		Case 9
			; round torus like thing
			For t= 1 To numdots
				xd = Rnd(-90,90)
				x0 = (Cos(xd)* 10) * (Cos(t*360.0/numdots) * 10)
				y0 = (Cos(t*360.0/numdots) * 10) * (Sin(t*360.0/numdots) * 10)
				z0 = Sin(xd) * 100
				points(t,1) = x0
				points(t,2) = y0
				points(t,3) = z0
			Next
		Case 10
			; wavey plain
			For t= 1 To numdots
				xd = Rnd(-90,90)
				x0 = (Cos(xd)* 10) * (Cos(t*360.0/numdots) * 10)
				y0 = (Cos(t*360.0/numdots) * 10) * (Sin(xd) * 10)
				z0 = Sin(xd) * 100
				points(t,1) = x0
				points(t,2) = y0
				points(t,3) = z0
			Next
		Case 11
			; round torus like thing
			For t= 1 To numdots
				xd = Rnd(-90,90)
				x0 = (Cos(t*360.0/numdots)* 10) * (Cos(t*360.0/numdots) * 10)
				y0 = (Cos(xd) * 10) * (Sin(xd) * 10)
				z0 = Sin(t*360.0/numdots) * 100
				points(t,1) = x0
				points(t,2) = y0
				points(t,3) = z0
			Next
		Case 12
			; squished pillow kinda thing :)
			For t= 1 To numdots
				xd = Rnd(-90,90)
				x0 = (Cos(xd)* 10) * (Cos(t*360.0/numdots) * 10)
				y0 = (Sin(xd) * 10) * (Sin(t*360.0/numdots) * 10)
				z0 = Sin(xd) * 100
				points(t,1) = x0
				points(t,2) = y0
				points(t,3) = z0
			Next
		Case 13
			; plain
			For t= 1 To numdots
				xd = Rnd(-90,90)
				x0 = (Cos(xd)* 10) * (Cos(xd) * 10)
				y0 = (Sin(xd) * 10) * (Sin(xd) * 10)
				z0 = Sin(t*360.0/numdots) * 100
				points(t,1) = x0
				points(t,2) = y0
				points(t,3) = z0
			Next
		Case 14
			; hmm
			For t= 1 To numdots
				xd = Rnd(-90,90)
				x0 = (Cos(xd)* 10) * (Cos(xd) * 10)
				y0 = (Sin(t*360.0/numdots) * 10) * (Sin(xd) * 10)
				z0 = Sin(t*360.0/numdots) * 100
				points(t,1) = x0
				points(t,2) = y0
				points(t,3) = z0
			Next
		Case 15
			; cool
			For t= 1 To numdots
				xd = Rnd(-90,90)
				x0 = (Sin(xd)* 10) * (Cos(xd) * 10)
				y0 = (Sin(xd) * 10) * (Sin(t*360.0/numdots) * 10)
				z0 = Sin(t*360.0/numdots) * 100
				points(t,1) = x0
				points(t,2) = y0
				points(t,3) = z0
			Next
		Case 16
			; yo
			For t= 1 To numdots
				xd = Rnd(-90,90)
				x0 = (Cos(t*360.0/numdots)* 10) * (Cos(t*360.0/numdots) * 10)
				y0 = (Sin(t*360.0/numdots) * 10) * (Sin(xd) * 10)
				z0 = Sin(t*360.0/numdots) * 100
				points(t,1) = x0
				points(t,2) = y0
				points(t,3) = z0
			Next
		Case 17
			; plain
			For t= 1 To numdots
				xd = Rnd(-90,90)
				x0 = (Cos(xd)* 10) * (Cos(t*360.0/numdots) * 10)
				y0 = (Sin(t*360.0/numdots) * 10) * (Sin(xd) * 10)
				z0 = Cos(xd) * 100
				points(t,1) = x0
				points(t,2) = y0
				points(t,3) = z0
			Next
		Case 18
			; bo-tie
			For t= 1 To numdots
				xd = Rnd(-90,90)
				x0 = (Cos(xd)* 10) * (Cos(xd) * 10)
				y0 = (Cos(t*360.0/numdots) * 10) * (Sin(t*360.0/numdots) * 10)
				z0 = Sin(t*360.0/numdots) * 100
				points(t,1) = x0
				points(t,2) = y0
				points(t,3) = z0
			Next
	End Select
End Function

Function morphing()
	For t = 1 To numdots
		If points(t,1) > tpoint(t,1)
			tpoint(t,1) = tpoint(t,1) + 1
		EndIf
		If points(t,1) < tpoint(t,1)
			tpoint(t,1) = tpoint(t,1) - 1
		EndIf
		If points(t,2) > tpoint(t,2)
			tpoint(t,2) = tpoint(t,2) + 1
		EndIf
		If points(t,2) < tpoint(t,2)
			tpoint(t,2) = tpoint(t,2) - 1
		EndIf
		If points(t,3) > tpoint(t,3)
			tpoint(t,3) = tpoint(t,3) + 1
		EndIf
		If points(t,3) < tpoint(t,3)
			tpoint(t,3) = tpoint(t,3) - 1
		EndIf
	Next
End Function

Function SaveData()
	dir$ = GetEnv$("APPDATA") + "\Jason's Screensavers"
	If FileType(dir$) = 0 Then CreateDir(dir$)
	file = WriteFile(dir$ + "\Morphing Dots.txt")
		WriteShort(file,numdots)
		WriteByte(file,r)
		WriteByte(file,FPS)
	CloseFile(file)
End Function

Function ReadData()
	dir$ = GetEnv$("APPDATA") + "\Jason's Screensavers"
	If FileType(dir$) = 0 Then CreateDir(dir$)
	If FileType(dir$ + "\Morphing Dots.txt") = 0 Then
		file = WriteFile(dir$ + "\Morphing Dots.txt")
			WriteShort(file,5000)
			WriteByte(file,2)
			WriteByte(file,200)
		CloseFile(file)
	End If
	file = ReadFile(dir$ + "\Morphing Dots.txt")
		numdots = ReadShort(file)
		r = ReadByte(file)
		FPS = ReadByte(file)
	CloseFile(file)
End Function