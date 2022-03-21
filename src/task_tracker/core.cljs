(ns task-tracker.core
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :as re-frame]
   [task-tracker.events :as events]
   [task-tracker.routes :as routes]
   [task-tracker.views :as views]
   [task-tracker.config :as config]
   ))


(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [views/main-panel] root-el)))

(defn init []
  (routes/start!)
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root))
