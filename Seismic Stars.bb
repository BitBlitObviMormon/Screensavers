;a not so simple scrolling backdrop with parallax effect
; Verified 1.48 4/18/2001
;hit ESC to exit

If Trim$(CommandLine$()) = "/c" Or Trim$(CommandLine$()) = "" Then
	.prompt
	sped = Input("How fast? (1-100) ")
	If sped < 1 Then sped = 1
	If FileType(GetEnv$("APPDATA") + "\Jason's Screensavers") = 0 Then CreateDir(GetEnv$("APPDATA") + "\Jason's Screensavers")
	dir$ = GetEnv$("APPDATA") + "\Jason's Screensavers\"
	file = WriteFile(dir$ + "Seismic Stars.txt")
	WriteShort(file,sped)
	End
End If

If Trim$(CommandLine$()) <> "/S" Then End

;Change this to make it shift faster
Global speed# = Float#(ReadTheStars())/100

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
y#=0
x#=0
vx#=0
vy#=0
mouse_x = MouseX()
mouse_y = MouseY()
SeedRnd(MilliSecs())
;loop until ESC hit
While Not KeyDown(1)
	e = WaitEvent(1)
	If e = $101 Or e = $201 Or e = $204 Or mouse_x <> MouseX() Or mouse_y <> MouseY() Then End
	;draw the backdrop
	TileBlock backdrop1,0+Int(x#),Int(y#)
	
	;draw it again, but across a bit and moving 'faster'
	TileImage backdrop2,0+Int(x#*2),Int(y#*2)
	
	;and again!
	TileImage backdrop3,0+Int(x#*3),Int(y#*3)
	
	;and yet again!
	TileImage backdrop4,0+Int(x#*4),Int(y#*4)
		
	;scroll the backdrop
	vx#=vx#+Rnd#(-speed#/Abs(bound#(vx#,123456, -.5)*2),speed#/(bound#(vx#, .5)*2))
	vy#=vy#+Rnd#(-speed#/Abs(bound#(vy#,123456, -.5)*2),speed#/(bound#(vy#, .5)*2))
	x#=x#+vx#
	y#=y#+vy#
	If y#>=ImageHeight(backdrop1) Then y#=y#-ImageHeight(backdrop1)
	If x#>=ImageWidth(backdrop1) Then x#=x#-ImageWidth(backdrop1)
	If y#<=-ImageHeight(backdrop1) Then y#=y#+ImageHeight(backdrop1)
	If x#<=-ImageWidth(backdrop1) Then x#=x#+ImageWidth(backdrop1)
	;flip the front and back buffers
	FlipCanvas canvas
Wend

;This function sets boundaries.
;If the number is lesser than the minimum then it is set to the minimum.
;If the number is greater than the maximum then it is set to the maximum.
Function Bound#(number#, min# = 123456, max# = 123456)
	If min# <> 123456 Then
		If number# < min# Then number# = min#
	End If
	If max# <> 123456 Then
		If number# > max# Then number# = max#
	End If
	Return number#
End Function

Function ReadTheStars()
	If FileType(GetEnv$("APPDATA") + "\Jason's Screensavers") = 0 Then CreateDir(GetEnv$("APPDATA") + "\Jason's Screensavers")
	dir$ = GetEnv$("APPDATA") + "\Jason's Screensavers\"
	If FileType(dir$ + "Seismic Stars.txt") = 0 Then
		file = WriteFile(dir$ + "Seismic Stars.txt")
		WriteShort(file,5)
		CloseFile(file)
	End If
	file = ReadFile(dir$ + "Seismic Stars.txt")
	Return ReadShort(file)
	CloseFile(file)
End Function