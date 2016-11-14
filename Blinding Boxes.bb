If Trim$(CommandLine$()) <> "/S" Then End

desktopwidth = GadgetWidth(Desktop())
desktopheight = GadgetHeight(Desktop())
max = Int((100 * desktopwidth * desktopheight) / 307200)
Const FPS = 1000
window = CreateWindow("Screen Saver",0,0,desktopwidth,desktopheight,0,0)
canvas = CreateCanvas(0,0,desktopwidth,desktopheight,window)
timer = CreateTimer(FPS)
SetBuffer(CanvasBuffer(canvas))
HidePointer canvas

FlushKeys()
FlushMouse()
FlushEvents()

Repeat
	e = WaitEvent()
	If e = $101 Or e = $102 Or e = $103 Or e = $201 Or e = $202 Or e = $203 Or e = $204 Then End
	If e = $4001 Then ;Timer
		If EventSource() = timer Then
			For a = 1 To max
				Color Rnd (100, 255), Rnd (100, 255), Rnd (100, 255)
				Rect Rand (GraphicsWidth()), Rand (GraphicsWidth()), 64, 64
			Next
			FlipCanvas canvas
		End If
	End If
Forever