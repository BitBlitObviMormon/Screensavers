AppTitle "Bouncing Particles"

Global timer = CreateTimer(120)

Dim heights(30)
heights(1)  = 6
heights(2)  = 26
heights(3)  = 58
heights(4)  = 105
heights(5)  = 164
heights(6)  = 237
heights(7)  = 323
heights(8)  = 423
heights(9)  = 536
heights(10) = 662
heights(11) = 801
heights(12) = 954
heights(13) = 1120
heights(14) = 1300
heights(15) = 1493
heights(16) = 1699
heights(17) = 1918
heights(18) = 2151
heights(19) = 2397
heights(20) = 2657
heights(21) = 2929
heights(22) = 3216
heights(23) = 3515
heights(24) = 3828
heights(25) = 4154
heights(26) = 4494
heights(27) = 4846
heights(28) = 5213
heights(29) = 5592
heights(30) = 5985

If Trim$(CommandLine$()) = "/c" Or Trim$(CommandLine$()) = "" Then
	.prompt
	particles = Input("Max Particles? (10-1000) ")
	If particles < 1 Then particles = 1
	If particles < 10 Then If Proceed("Are you sure? Less than 10 can be boring.") < 1 Then Goto prompt
	If particles > 1000 Then If Proceed("Are you sure? More than 1000 can make your computer slow.",True) < 1 Then Goto prompt
	If FileType(GetEnv$("APPDATA") + "\Jason's Screensavers") = 0 Then CreateDir(GetEnv$("APPDATA") + "\Jason's Screensavers")
	dir$ = GetEnv$("APPDATA") + "\Jason's Screensavers\"
	file = WriteFile(dir$ + "Bouncing Particles.txt")
	WriteShort(file,particles)
	End
End If

Global max_particles=GetMaxParticles()

Global width=GadgetWidth(Desktop()), height=GadgetHeight(Desktop()), gravity#=.075

Global decay = 1
Global max = height
Global maxv = 0
Global minv = 0

Global count

While (heights(maxv) < height And maxv <= 30)
	maxv = maxv + 1
Wend
maxv = -maxv + 1

While (heights(minv) < height / 2 And minv <= 30)
	minv = minv + 1
Wend
minv = -minv + 1

Type Particle
	Field x#,y#
	Field xs#,ys#
	Field w,h,r,g,b
End Type

If Trim$(CommandLine$()) <> "/S" Then End

window = CreateWindow("Screen Saver",0,0,width,height,0,0)
canvas = CreateCanvas(0,0,width,height,window)
HidePointer canvas
SetBuffer CanvasBuffer(canvas)

Setup(width/2)

mouse_x = MouseX()
mouse_y = MouseY()

Repeat
	e = WaitEvent()
	If e = $101 Or e = $201 Or e = $204 Or mouse_x <> MouseX() Or mouse_y <> MouseY() Then End
	If e = $4001 Then 
		If EventSource() = timer
			Cls
			Update()
			Render()
;			Text 0,0,count
			FlipCanvas canvas
		End If
	End If
Forever

End

Function Setup(x)
	If count>=max_particles Then Return
	p.Particle=New Particle
	p\w=Rnd(5)+5:p\h=Rnd(5)+5
	p\x=x:p\y=height
	p\xs=Rnd(-2,2):p\ys=Rnd(minv, maxv)
	p\r=255:p\g=255:p\b=255
	count=count+1
End Function

Function Update()
	For p.Particle=Each Particle
		p\x=p\x+p\xs
		If p\x<0 Or p\x>width-p\w
			p\xs=-p\xs
			p\x=p\x+p\xs
		EndIf
		p\ys=p\ys+gravity:p\y=p\y+p\ys
		If p\y>height-p\h And p\ys>0 Then Setup( p\x ):p\ys=-p\ys
		If p\b>0
			p\b=p\b-decay
		Else If p\g>0 
			p\g=p\g-decay
		Else If p\r>0
			p\r=p\r-1
		Else 
			Delete p:count=count-1
		EndIf
	Next
End Function

Function Render()
	For p.Particle=Each Particle
		Color p\r,p\g,p\b
		Rect p\x,p\y,p\w,p\h
	Next
End Function

Function GetMaxParticles()
	If FileType(GetEnv$("APPDATA") + "\Jason's Screensavers") = 0 Then CreateDir(GetEnv$("APPDATA") + "\Jason's Screensavers")
	dir$ = GetEnv$("APPDATA") + "\Jason's Screensavers\"
	If FileType(dir$ + "Bouncing Particles.txt") = 0 Then
		file = WriteFile(dir$ + "Bouncing Particles.txt")
		WriteShort(file,50)
		CloseFile(file)
	End If
	file = ReadFile(dir$ + "Bouncing Particles.txt")
	Return ReadShort(file)
	CloseFile(file)
End Function