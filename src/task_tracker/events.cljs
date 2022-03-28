(ns task-tracker.events
  (:require
   [re-frame.core :as re-frame]
   [task-tracker.db :as db]
   [task-tracker.task.core :as task]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   ))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
   db/default-db))

(re-frame/reg-event-fx
  ::navigate
  (fn-traced [_ [_ handler]]
   {:navigate handler}))

(re-frame/reg-event-fx
 ::set-active-panel
 (fn-traced [{:keys [db]} [_ active-panel]]
   {:db (assoc db :active-panel active-panel)}))

(re-frame/reg-event-fx
  ::increment-task-status
  (fn-traced [{:keys [db]} [_ task-id]]
    (let [task (get-in db [:tasks :tasks task-id])
          status (:status task)
          status-length (count status)
          incremented-status (+ status 1)
          new-status (if (> incremented-status status-length) status-length incremented-status)
          type ((:type task) task/task-types)
          ] {:db (assoc-in db [:tasks :tasks task-id] {:status new-status} )})))

(re-frame/reg-event-fx
  ::decrement-task-status
  (fn-traced [{:keys [db]} [_ task-id]]
    (let [task (get-in db [:tasks :tasks task-id])
          status (:status task)
          status-length (count status)
          incremented-status (- status 1)
          new-status (if (< incremented-status 0) 0 incremented-status)
          type ((:type task) task/task-types)
          ] {:db (assoc-in db [:tasks :tasks task-id] {:status new-status} )})))
