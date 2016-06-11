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

(defn fill-rect [x y color]
  (set! (.-fillStyle ctx) color)
  (.fillRect ctx x y 2 2))



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

(def cycle-color
  (tconcat (.takeUntil red mouse-click)
          (defer #(tconcat (.takeUntil blue mouse-click)
                          cycle-color))))

(-> (.zip sine-wave cycle-color #(vector % %2))
    (.take 600)
    (.subscribe (fn [[{:keys [x y]} color]]
                  (fill-rect x y color))))

(defn write-text []
  (set! (.-font ctx) "20px Arial")
  (set! (.-fillStyle ctx) "white")
  (.fillText ctx "Drugs" 530 780))

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
