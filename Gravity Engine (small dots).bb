
;Debug OFF for this beast!
;
;right-mouse zoom out, left-mouse zoom in.
;
Global width=GadgetWidth(Desktop()), height=GadgetHeight(Desktop())
Const min_mass#=.01,max_mass#=.02

Type Blob
	Field x#,y#,xs#,ys#,mass#,r,g,b
End Type

If Trim$(CommandLine$()) = "/c" Or Trim$(CommandLine$()) = "" Then
	.prompt
	blobs = Input("Number of planets? (50-300) ")
	If blobs < 1 Then blobs = 1
	If blobs < 50 Then If Proceed("Are you sure? Less than 50 can be boring.") < 1 Then Goto prompt
	If blobs > 300 And blobs <= 500 Then If Proceed("Are you sure? More than 300 may make your computer slow or disfunctional!",True) < 1 Then Goto prompt
	If blobs > 500 Then If Proceed("Are you sure? More than 500 will likely crash your system!",True) < 1 Then Goto prompt
	If FileType(GetEnv$("APPDATA") + "\Jason's Screensavers") = 0 Then CreateDir(GetEnv$("APPDATA") + "\Jason's Screensavers")
	dir$ = GetEnv$("APPDATA") + "\Jason's Screensavers\"
	file = WriteFile(dir$ + "Gravity (small).txt")
	WriteShort(file,blobs)
	CloseFile(file)
	End
End If

If Trim$(Lower$(CommandLine$())) <> "/s" Then End

window = CreateWindow("Screen Saver",0,0,width,height,0,0)
canvas = CreateCanvas(0,0,width,height,window)
SetBuffer CanvasBuffer(canvas)
HidePointer canvas
CreateTimer(240)

SeedRnd(MilliSecs())

SetupBlobs(ReadTheStars())

Global scale#=1

;MoveMouse(GraphicsWidth() / 2, GraphicsHeight() / 2)

mouse_x = GraphicsWidth() / 2
mouse_y = GraphicsHeight() / 2

FlushKeys()
FlushMouse()
FlushEvents()

While Not KeyDown(1)
	e = WaitEvent(1)
	If e = $101 Or e = $201 Or e = $204 Or e = $203 Then End
	If e = $4001 Then
		Cls
		Origin GraphicsWidth() / 2, GraphicsHeight() / 2
		time=MilliSecs()
		UpdateBlobs()
		RenderBlobs()
		FlipCanvas canvas
	End If
Wend

End

Function SetupBlobs(num_blobs)
	For k=1 To num_blobs
		b.Blob=New Blob
		ty#=Rnd(1)
		ra#=ty*width
		an#=Rnd(360)
		ma#=Rnd(1)*(max_mass-min_mass)+min_mass
		b\x=Cos(an)*ra / 3
		b\y=Sin(an)*ra / 3
		b\xs#=0:b\ys#=0
		b\mass=ma
		t#=(ma-min_mass)/(max_mass-min_mass)*255
		b\r=t
		b\g=t
		b\b=255
	Next
End Function

Function UpdateBlobs()
	For b.Blob=Each Blob
		For t.Blob=Each Blob
			If t=b Then Exit
			dx#=b\x-t\x
			dy#=b\y-t\y
			sq#=1.0/(dx*dx+dy*dy)
			t\xs=t\xs+dx*(b\mass*sq)
			t\ys=t\ys+dy*(b\mass*sq)
			b\xs=b\xs-dx*(t\mass*sq)
			b\ys=b\ys-dy*(t\mass*sq)
		Next
	Next
End Function

Function RenderBlobs()
	For b.Blob=Each Blob
		b\x=b\x+b\xs
		b\y=b\y+b\ys
		Color b\r,b\g,b\b
		Plot b\x, b\y
		;Rect b\x*scale-1,b\y*scale-1,3,3
	Next
End Function

Function ReadTheStars()
	If FileType(GetEnv$("APPDATA") + "\Jason's Screensavers") = 0 Then CreateDir(GetEnv$("APPDATA") + "\Jason's Screensavers")
	dir$ = GetEnv$("APPDATA") + "\Jason's Screensavers\"
	If FileType(dir$ + "Gravity (small).txt") = 0 Then
		file = WriteFile(dir$ + "Gravity (small).txt")
		WriteShort(file,50)
		CloseFile(file)
	End If
	file = ReadFile(dir$ + "Gravity (small).txt")
	retval = ReadShort(file)
	CloseFile(file)
	Return retval
End Function