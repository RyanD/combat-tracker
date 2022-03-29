(ns task-tracker.events
  (:require
   [re-frame.core :as rf]
   [task-tracker.db :as db]
   [task-tracker.tasks.core :as task]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   ))

(rf/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
   db/default-db))

(rf/reg-event-fx
  ::navigate
  (fn-traced [_ [_ handler]]
   {:navigate handler}))

(rf/reg-event-fx
 ::set-active-panel
 (fn-traced [{:keys [db]} [_ active-panel]]
   {:db (assoc db :active-panel active-panel)}))

(rf/reg-event-fx
  ::increment-task-status
  (fn-traced [{:keys [db]} [_ task-id]]
    (let [task (get-in db [:tasks :tasks task-id])
          status (:status task)
          type ((:type task) task/task-types)
          status-length (- (count type) 1)
          incremented-status (+ status 1)
          new-status (if (>= incremented-status status-length) status-length incremented-status)
          ]
      {:db (assoc-in db [:tasks :tasks task-id :status] new-status)})))

(rf/reg-event-fx
  ::decrement-task-status
  (fn-traced [{:keys [db]} [_ task-id]]
    (let [task (get-in db [:tasks :tasks task-id])
          _ (println task)
          status (:status task)
          type ((:type task) task/task-types)
          status-length (- (count type) 1)
          decremented-status (max 0 (- status 1))
          _ (println (str decremented-status ":" status-length))
          new-status (if (<= decremented-status 0) 0  decremented-status )
          ]
      {:db (assoc-in db [:tasks :tasks task-id :status] new-status)})))
