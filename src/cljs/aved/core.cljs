(ns aved.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))

(enable-console-print!)

(.log js/console "hello CS")
(.log js/console "PLOP is here")
(.log js/console "SOME MORE THINGS")
(.log js/console "SOME more and more")
(.log js/console "SOME more and morei 2222222")

(def canvas (.getElementById js/document "myCanvas"))
(def ctx (.getContext canvas "2d"))

;; Clear canvas before doing anything else
(.clearRect ctx 0 0 (.-width canvas) (.-height canvas))

(def interval js/Rx.Observable.interval)
(def ttime (interval 10))

(-> ttime
    (.take 5)
    (.subscribe (fn [n]
                  (.log js/console n))))

(defn deg-to-rad [n]
  (* (/ Math/PI 180) n))

(defn sine-coord [x]
  (let [sin (Math/sin (deg-to-rad x))
        y (- 100 (* sin 90))]
    {:x x
     :y y
     :sin sin}))

(.log js/console (str (sine-coord 50)))

(def sine-wave
  (.map ttime sine-coord))

(-> sine-wave
    (.take 5)
    (.subscribe (fn [xysin]
                  (.log js/console (str xysin)))))


;;ctx.beginPath();
;;ctx.arc(100,75,50,0,2*Math.PI);
;;ctx.stroke(); ;; draws the border line
;; arc (x, y, radius, start angle[rad], ending angle[rad])
(defn fill-circle [x y color size]
  (set! (.-fillStyle ctx) color)
  (set! (.-lineWidth ctx) 0)
  (.beginPath ctx)
  (.arc ctx x y size 0 (* 2 Math/PI))
  (.fill ctx)
  )

(fill-circle 300 300 "red" 20)
(fill-circle 300 300 "yellow" 10)
(fill-circle 300 300 "blue" 5)

(defn fill-rect [x y color pixel]
  (set! (.-fillStyle ctx) color)
  (.fillRect ctx x y 2 2))

;; fill rect for 1 pixel size
(defn fill-rect1 [x y color]
  (fill-rect x y color 1))

(defn fill-rect2 [x y color]
  (fill-rect x y color 2))


(def color (.map sine-wave
                 (fn [{:keys [sin]}]
                   (if (< sin 0)
                     "red"
                     "blue"))))

(def violet (.map ttime (fn [_] "violet")))
(def indigo (.map ttime (fn [_] "indigo")))
(def blue (.map ttime (fn [_] "blue")))
(def green (.map ttime (fn [_] "green")))
(def yellow (.map ttime (fn [_] "yellow")))
(def orange (.map ttime (fn [_] "orange")))
(def red (.map ttime (fn [_] "red")))

(def tconcat js/Rx.Observable.concat)
(def defer js/Rx.Observable.defer)
(def from-event js/Rx.Observable.fromEvent)

(def mouse-click (from-event canvas "click"))

(def mouse-over (from-event canvas "over"))

(defn write-text []
  (set! (.-font ctx) "20px Arial")
  (set! (.-fillStyle ctx) "white")
  (.fillText ctx "Drugs" 780 780))

(write-text)

;; Rotate Text on CANVAS
;;context.save();
;;context.translate(newx, newy);
;;context.rotate(-Math.PI/2);
;;context.textAlign = "center";
;;context.fillText("Your Label Here", labelXposition, 0);
;;context.restore();

(defn write-rotated-text []
  (.save ctx)
  (.translate ctx 500 500)
  (.rotate ctx (/ (* -1 Math/PI) 2))
  (set! (.-textAlign ctx) "center")
  (set! (.-font ctx) "20px Arial")
  (set! (.-fillStyle ctx) "white")
  (.fillText ctx "Reactions" 100, -470)
  (.restore ctx))

(defn write-coordinate-labels []
  ())

(write-rotated-text)

(.log js/console "test new line ")
(.log js/console (str "Math.PI value:" Math/PI))

;;     context.beginPath();
;;      context.moveTo(100, 150);
;;      context.lineTo(450, 50);
;;      context.stroke();
(defn draw-line [x1 y1 x2 y2]
  (.beginPath ctx)
  (.moveTo ctx x1 y1)
  (.lineTo ctx x2 y2)
  (set! (.-strokeStyle ctx) "#B4B4B4")
  (.stroke ctx))

(def horizontal-grid-lines [{:x1 1450 :y1 600 :x2 150 :y2 600}])
(def vertical-grid-lines [{:x1 200 :y1 650 :x2 200 :y2 50 }])
(def horizontal-lines 6)
(def vertical-lines 13)

(doseq [{x1 :x1 y1 :y1 x2 :x2 y2 :y2} horizontal-grid-lines]
  (do
    (.log js/console (str "x1=" x1 " y1=" y1 " x2=" x2 " y2=" y2))
    (draw-line x1 y1 x2 y2)))

;;
;; draw the horizontal grid lines
;;
(doseq [{x1 :x1 y1 :y1 x2 :x2 y2 :y2} horizontal-grid-lines]
  (dotimes [n horizontal-lines] (.log js/console "Index: " n)
           (draw-line x1 (- y1 (* n 100)) x2 (- y2 (* n 100)))))

;;
;; draw the vertical grid lines
;;
(doseq [{x1 :x1 y1 :y1 x2 :x2 y2 :y2} vertical-grid-lines]
  (dotimes [n vertical-lines] (.log js/console "Index: " n)
           (draw-line (+ x1 (* n 100)) y1 (+ x2 (* n 100)) y2)))
