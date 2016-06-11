(ns aved.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))

(enable-console-print!)

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
  (.fill ctx))

(defn fill-rect [x y w h color]
  (set! (.-fillStyle ctx) color)
  (.fillRect ctx x y w h))

;; fill rect for 1 pixel size
(defn fill-rect1 [x y w h color]
  (fill-rect x y w h color))

(defn fill-rect2 [x y w h color]
  (fill-rect x y w h color))


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

(defn write-text [text x y]
  (set! (.-font ctx) "20px Arial")
  (set! (.-fillStyle ctx) "white")
  (.fillText ctx text x y))

(defn write-text-s1 [text x y]
  (set! (.-font ctx) "13px Arial")
  (set! (.-fillStyle ctx) "white")
  (.fillText ctx text x y))

(defn write-text-s1-black [text x y]
  (set! (.-font ctx) "13px Arial")
  (set! (.-fillStyle ctx) "black")
  (.fillText ctx text x y))

(write-text "Drugs" 780 710)
(def right-col-legends {:DE "Death"
                        :LT "Life Threatening"
                        :HO "Hospitalization"
                        :DS "Disability"
                        :CA "Congenetial Anomaly"
                        :RI "Required Intervention"
                        :OT "Other"})

(defn serialize [m sep] (str (clojure.string/join sep (map (fn [[k v]] (str (name k) " " v)) m)) "\n"))
(.log js/console (str "name = " (serialize right-col-legends " | ")))
(def right-col-legend-str (serialize right-col-legends " | "))

(write-text-s1 right-col-legend-str 730 780)

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
  (set! (.-strokeStyle ctx) "#3E3E3E")
  (.stroke ctx))

(def horizontal-grid-lines [{:x1 1400 :y1 600 :x2 150 :y2 600}])
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

(fill-rect 1440 20 40 100 "red")
(fill-rect 1440 120 40 100 "orange")
(fill-rect 1440 220 40 100 "#F5D282")
(fill-rect 1440 320 40 100 "#F5F38C")
(fill-rect 1440 420 40 100 "#B1D8FA")
(fill-rect 1440 520 40 100 "#3874A8")
(fill-rect 1440 620 40 100 "#7D4BBF")

(write-text-s1-black "DE" 1450 74)
(write-text-s1-black "LT" 1450 174)
(write-text-s1-black "HO" 1450 274)
(write-text-s1-black "DS" 1450 374)
(write-text-s1-black "CA" 1450 474)
(write-text-s1-black "RI" 1450 574)
(write-text-s1-black "OT" 1450 674)

(write-text-s1 "AVANDIA" 175 670)
(write-text-s1 "ACTOS" 275 670)
(write-text-s1 "BONIVA" 375 670)
(write-text-s1 "PRADAXA" 475 670)
(write-text-s1 "SEROQUEL" 575 670)

(write-text-s1 "Myocordial Infarction" 40 600)
(write-text-s1 "Bladder Cancer" 40 500)
(write-text-s1 "Cardiac Failure" 40 430)
(write-text-s1 "Gastrointestinal" 40 380)
(write-text-s1 "Hemorrihage" 40 393)
(write-text-s1 "Cerebuvascular Accident" 40 300)
(write-text-s1 "Death" 40 250)
(write-text-s1 "Coronary Artery Disease" 40 210)
(write-text-s1 "Femur Fracture" 40 180)
(write-text-s1 "Haematuria" 40 160)
(write-text-s1 "Pancreatitis" 40 140)
(write-text-s1 "Type 2" 40 100)
(write-text-s1 "Diabetes Meilitus" 40 120)

(fill-circle 300 300 "red" 20)
(fill-circle 300 300 "yellow" 10)
(fill-circle 300 300 "blue" 5)
(fill-circle 400 400 "blue" 5)
(fill-circle 400 500 "green" 2)
(fill-circle 300 300 "yellow" 1)
(fill-circle 380 60 "yellow" 2)
(fill-circle 700 320 "yellow" 8)
(fill-circle 900 400 "yellow" 7)
(fill-circle 1000 600 "red" 10)
(fill-circle 800 550 "yellow" 1)
(fill-circle 800 560 "yellow" 1)
(fill-circle 900 500 "red" 5)
(fill-circle 1000 100 "red" 5)
(fill-circle 600 200 "yellow" 3)
