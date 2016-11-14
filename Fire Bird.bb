If Trim$(CommandLine$()) = "/c" Or Trim$(CommandLine$()) = "" Then
	.prompt
	stars = Input("Intensity? (1-20) ")
	If stars < 1 Then stars = 1
	If stars > 1000 Then If Proceed("Are you sure? More than 20 can lag!",True) < 1 Then Goto prompt
	If FileType(GetEnv$("APPDATA") + "\Jason's Screensavers") = 0 Then CreateDir(GetEnv$("APPDATA") + "\Jason's Screensavers")
	dir$ = GetEnv$("APPDATA") + "\Jason's Screensavers\"
	file = WriteFile(dir$ + "Fire Bird.txt")
	WriteShort(file,stars)
	CloseFile(file)
	End
End If

If Trim$(Lower$(CommandLine$())) <> "/s" Then End

Global FPS = 60
Global intensity = ReadTheStars()

Global width=GadgetWidth(Desktop()), height=GadgetHeight(Desktop()), gravity#=.1

window = CreateWindow("Screen Saver",0,0,width,height,0,0)
canvas = CreateCanvas(0,0,width,height,window)
Global timer = CreateTimer(FPS)

HidePointer canvas
SetBuffer CanvasBuffer(canvas)

Type Frag
	Field x#,y#,xs#,ys#
	Field r,g,b
End Type

vx = 0
vy = 0
x = width/2
y = height/2

FlushEvents
Repeat
	
	e = WaitEvent()
	If e = $101 Or e = $201 Or e = $203 Or e = $204 Then
		End
	Else If e = $4001 Then
		If EventSource() = timer Then
			UpdateFrags()
			
			; Create a particle if the mdown variable was set in the event loop...
			vx = vx + Rand(-1, 1)
			vy = vy + Rand(-1, 1)
			x = x + vx
			y = y + vy
			If x > width Then vx = vx - 1
			If x < 0 Then vx = vx + 1
			If y > height Then vy = vy - 1
			If y < 0 Then vy = vy + 1
			If vx > Int(width / 200) Then vx = vx - 1
			If vx < -Int(width / 200) Then vx = vx + 1
			If vy > Int(width / 200) Then vy = vy - 1
			If vy < -Int(width / 200) Then vy = vy + 1

			Cls
			CreateFrags(x, y)
			RenderFrags()
			FlipCanvas canvas
		End If
	End If
Forever

End

Function CreateFrags(x, y)
	count=Rnd(intensity)+intensity
	anstep#=360.0/count
	an#=Rnd(anstep)
	For k=1 To count
		f.Frag=New Frag
		f\x=x
		f\y=y
		f\xs=Cos( an ) * Rnd( 3,4 )
		f\ys=Sin( an ) * Rnd( 3,4 )
		f\r=255:f\g=255:f\b=255
		an=an+anstep
	Next
End Function

Function UpdateFrags()
	For f.Frag=Each Frag
		f\x=f\x+f\xs
		f\y=f\y+f\ys
		f\ys=f\ys+gravity
		If f\x<0 Or f\x>=width Or f\y>=height
			Delete f
		Else If f\b>0
			f\b=f\b-5
		Else If f\g>0
			f\g=f\g-3
		Else If f\r>0
			f\r=f\r-1
			If f\r=0 Then Delete f
		EndIf
	Next
End Function

Function RenderFrags()
	For f.Frag=Each Frag
		Color f\r,f\g,f\b
		Rect f\x-1,f\y-1,3,3
	Next
End Function

Function ReadTheStars()
	If FileType(GetEnv$("APPDATA") + "\Jason's Screensavers") = 0 Then CreateDir(GetEnv$("APPDATA") + "\Jason's Screensavers")
	dir$ = GetEnv$("APPDATA") + "\Jason's Screensavers\"
	If FileType(dir$ + "Fire Bird.txt") = 0 Then
		file = WriteFile(dir$ + "Fire Bird.txt")
		WriteShort(file,1)
		CloseFile(file)
	End If
	file = ReadFile(dir$ + "Fire Bird.txt")
	retval = ReadShort(file)
	CloseFile(file)
	Return retval
End Function