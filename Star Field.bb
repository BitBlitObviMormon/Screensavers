;Starfield demo
;Copyright ©2000 EdzUp
;written by Ed Upton
;Graphics 'Borrowed' from Mark Sibly's stars.bmp

;adjust for more or less stars (500 looks nice)
;I can get 2500 stars without losing a single frame

Const sqrt2# = 1.4142135623730950488016887242097

If Trim$(CommandLine$()) = "/c" Or Trim$(CommandLine$()) = "" Then
	.prompt
	stars = Input("Number of stars? (100-1000) ")
	If stars < 1 Then stars = 1
	If stars < 100 Then If Proceed("Are you sure? Less than 100 can be boring.") < 1 Then Goto prompt
	If stars > 1000 Then If Proceed("Are you sure? More than 1000 can make your computer slow!",True) < 1 Then Goto prompt
	If FileType(GetEnv$("APPDATA") + "\Jason's Screensavers") = 0 Then CreateDir(GetEnv$("APPDATA") + "\Jason's Screensavers")
	dir$ = GetEnv$("APPDATA") + "\Jason's Screensavers\"
	file = WriteFile(dir$ + "Star Field.txt")
	WriteShort(file,stars)
	CloseFile(file)
	End
End If

If Trim$(CommandLine$()) <> "/S" Then End

Global staramount = ReadTheStars()

Global w = GadgetWidth(Desktop())
Global h = GadgetHeight(Desktop())
window = CreateWindow("Screen Saver",0,0,w,h,0,0)
canvas = CreateCanvas(0,0,w,h,window)
SetBuffer CanvasBuffer(canvas)
HidePointer canvas

CreateTimer(60)
Global stara=LoadImage("graphics\star1.bmp")
Global starb=LoadImage("graphics\star2.bmp")
Global starc=LoadImage("graphics\star3.bmp")

;star array
Type star
 Field rad,speed,angle
End Type

setupstars(w, h)

mouse_x = MouseX()
mouse_y = MouseY()

Const forgiveness = 19
Global sradius = w/24
Global delxp = w + forgiveness
Global delyp = h + forgiveness
Global delxn = -forgiveness
Global delyn = -forgiveness
Global ovalw = sradius * 2.0 * sqrt2#
Global ovalx = w/2 - ovalw/2
Global ovaly = h/2 - ovalw/2

;main loop
While Not KeyDown(1)
 e = WaitEvent()
 If e = $101 Or e = $201 Or e = $204 Or mouse_x <> MouseX() Or mouse_y <> MouseY() Then End
 If e = $4001 Then
  Cls
  update()
  FlipCanvas canvas
 End If
Wend
End

;set up stars
Function setupstars(w, h)
 For sc=0 To staramount
  stars.star = New star
  stars\rad=Rnd(w/2)+80
  stars\angle=Rnd(65535)
  stars\speed=Rnd(5)+1
 Next
End Function

;update each star in turn
Function update()
 For stars.star = Each star
  xx=w/2+Sin((2*stars\angle)*Pi/360)*stars\rad
  yy=h/2+Cos((2*stars\angle)*Pi/360)*stars\rad
  ;replace star if it goes off screen
  If xx<delxn Or xx>delxp Or yy<delyn Or yy>delyp
   stars\rad=Rnd(sradius)+sradius
   stars\angle=Rnd(65535)
   stars\speed=Rnd(5)+1
  EndIf
  stars\rad=stars\rad+(stars\speed*2)
  If stars\speed=1 Then DrawImage starc,xx,yy
  If stars\speed=2 Or stars\speed=3 Then DrawImage starb,xx,yy
  If stars\speed>3 Then DrawImage stara,xx,yy
;  If stars\speed=1 Then Color 50,50,50
;  If stars\speed=2 Then Color 100,100,100
;  If stars\speed=3 Then Color 150,150,150
;  If stars\speed=4 Then Color 200,200,200
;  If stars\speed=5 Then Color 250,250,250
;  Plot xx,yy
 Next
 Color 0,0,0
 Oval ovalx,ovaly,ovalw,ovalw
End Function

Function ReadTheStars()
	If FileType(GetEnv$("APPDATA") + "\Jason's Screensavers") = 0 Then CreateDir(GetEnv$("APPDATA") + "\Jason's Screensavers")
	dir$ = GetEnv$("APPDATA") + "\Jason's Screensavers\"
	If FileType(dir$ + "Star Field.txt") = 0 Then
		file = WriteFile(dir$ + "Star Field.txt")
		WriteShort(file,500)
		CloseFile(file)
	End If
	file = ReadFile(dir$ + "Star Field.txt")
	retval = ReadShort(file)
	CloseFile(file)
	Return retval
End Function