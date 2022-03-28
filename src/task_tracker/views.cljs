(ns task-tracker.views
  (:require
    [re-frame.core :as re-frame]
    [re-com.core :as re-com :refer [at]]
    [task-tracker.styles :as styles]
    [task-tracker.events :as events]
    [task-tracker.routes :as routes]
    [task-tracker.task.core :as task]
    [task-tracker.subs :as subs]))

;; home

(defn home-title []
  (let [name (re-frame/subscribe [::subs/name])]
    [re-com/title
     :src (at)
     :label "task-tracker"
     :level :level1
     :class (styles/level1)]))

(defn link-to-about-page []
  [re-com/hyperlink
   :src (at)
   :label "go to About Page"
   :on-click #(re-frame/dispatch [::events/navigate :about])])

(defn increment-button [task-id]
  [re-com/button
   :src       (at)
   :label    "+"
   :on-click #(re-frame/dispatch [::events/increment-task-status task-id])
   ])

(defn decrement-button [task-id]
  [re-com/button
   :src       (at)
   :label    "-"
   :on-click #(re-frame/dispatch [::events/decrement-task-status task-id])
   ])

(defn link-to-home-page []
  [re-com/hyperlink
   :src (at)
   :label "go to Home Page"
   :on-click #(re-frame/dispatch [::events/navigate :home])])

(defn tasks []
  (let [task-list @(re-frame/subscribe [::subs/tasks])
        tasks     (:tasks task-list)]
    [:<>
     (for [task tasks]
       (let [task (first (vals task ))
             task-type   ((:type task) task/task-types)
             task-id-kw (keyword (str (:id task)))
             task-status (nth task-type (int (:status task)))]
          ^{:key (str (:id task))} [:div  [:h4 (str (:qty task) "x " (:name task))]
          [:h6 [decrement-button task-id-kw][increment-button task-id-kw] task-status]]))]))

(defn home-panel []
  [re-com/v-box
   :src (at)
   :gap "1em"
   :children [[home-title]
              [tasks]
              ]])


(defmethod routes/panels :home-panel [] [home-panel])
;; main

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    [re-com/v-box
     :src (at)
     :height "100%"
     :children [(routes/panels @active-panel)]]))
