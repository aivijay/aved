(ns aved.test-runner
  (:require
   [doo.runner :refer-macros [doo-tests]]
   [aved.core-test]))

(enable-console-print!)

(doo-tests 'aved.core-test)
