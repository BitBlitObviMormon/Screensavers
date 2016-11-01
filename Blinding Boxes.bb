If Trim$(CommandLine$()) <> "/S" Then End

desktopwidth = GadgetWidth(Desktop())
desktopheight = GadgetHeight(Desktop())
Const FPS = 30
max = Int((100 * desktopwidth * desktopheight) / 307200)

window = CreateWindow("Screen Saver",0,0,desktopwidth,desktopheight,0,0)
canvas = CreateCanvas(0,0,desktopwidth,desktopheight,window)
timer = CreateTimer(FPS)
SetBuffer(CanvasBuffer(canvas))
HidePointer canvas

FlushKeys()
FlushMouse()
FlushEvents()

Repeat
	Select WaitEvent()
		Case $101
			End
		Case $102
			End
		Case $103
			End
		Case $201
			End
		Case $202
			End
		Case $203
			End
		Case $204
			End
		Case $4001
			If EventSource() = timer Then
				Cls
				For a = 1 To max
					Color Rnd (100, 255), Rnd (100, 255), Rnd (100, 255)
					Rect Rand (GraphicsWidth()), Rand (GraphicsWidth()), 64, 64
				Next
				FlipCanvas canvas
			End If
	End Select
Forever
