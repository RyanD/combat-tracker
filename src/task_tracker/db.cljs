(ns task-tracker.db
  (:require [task-tracker.tasks.rpg.icespire-peak :as ice]
            [task-tracker.tasks.core :as tasks]))

(def default-db
  {:name "re-frame"
   :tasks (tasks/create-task-map "Dragons of Icespire Peak" "" ice/tasks )})
