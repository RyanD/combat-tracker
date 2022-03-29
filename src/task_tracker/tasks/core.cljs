(ns task-tracker.tasks.core
  (:require [clojure.spec.alpha :as s]
            [reagent.core :as r]
            [cljs.pprint :as pprint]))

(def task-types {:rpg/printed-handout [:planned :designed :proofed :printed :packed]
                 :rpg/mini            [:planned :picked :purchased :possessed :primed :painted :packed]
                 :rpg/mini-fab        [:planned :parted :purchased :possessed :produced :primed :painted :packed]
                 :rpg/virtual-map     [:planned :designed :placed-in-module]
                 :rpg/actual-map      [:planned :prepared :proofed :printed :painted :packed]})

;; spec
(s/def :task/id uuid?)
(s/def :acct/title string?)
(s/def :acct/description string?)
(s/def :acct/type #{(keys task-types)})

;; the map
(def task-map {:id          #uuid"74ada7ce-c5c7-461e-9d02-5c190c937335"
               :name        ""
               :description ""
               :type        nil
               :status      0
               })


(defn- generate-task-vector [tasks]
  (into {} (for [task tasks]
         (let [id  (random-uuid)
               key (keyword (str id))]
           {key {:id     id
                :qty    (first task)
                :name   (nth task 1)
                :type   (nth task 2)
                :status (nth task 3)}}))))

(defn create-task-map [title description tasks]
  {:title       title
   :description description
   :tasks       (generate-task-vector tasks)})


(defn set-status [task new-status]
  (let [task-type (get (:type task) task-types)]
    (if (contains? task-type new-status)
      (swap! task assoc :status new-status)
      (pprint/pprint (str "Error: " new-status "not present in " (name (:type task)))))
    ))

(defn increment-task-status [task]
  (update-in task [:status] inc))

(defn decrement-taskmap-status [task]
  (update-in task [:status] dec))

(defn print-taskmap [task]
  (let [task-type   ((:type task) task-types)
        task-status (nth task-type (int (:status task)))]
    (pprint/pprint {:id          (:id task)
                    :name        (:name task)
                    :description (:description task)
                    :type        (:type task)
                    :status      task-status})))
