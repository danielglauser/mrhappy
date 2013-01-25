(ns mrhappy.test.websocket-server
    (:use clojure.test
        mrhappy.websocket-server))

(deftest strongsubj-positive
  (is (= (convert-to-happyness-percentage "strongsubj-positive") 100)))
(deftest weaksubj-positive
  (is (= (convert-to-happyness-percentage "weaksubj-positive") 80)))
(deftest strongsubj-neutral
  (is (= (convert-to-happyness-percentage "strongsubj-neutral") 60)))
(deftest weaksubj-neutral
  (is (= (convert-to-happyness-percentage "weaksubj-neutral") 40)))
(deftest weaksubj-negative
  (is (= (convert-to-happyness-percentage "weaksubj-negative") 20)))
(deftest strongsubj-negative
  (is (= (convert-to-happyness-percentage "strongsubj-negative") 0)))
