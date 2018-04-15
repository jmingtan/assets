(ns assets.server
  (:require [com.stuartsierra.component :as component]
            [ring.adapter.jetty :refer [run-jetty]]
            [assets.handler :refer [handler]]))

(defrecord Server [host port app]
  component/Lifecycle

  (start [this]
    (println ";; Starting web server at" (format "http://%s:%s" host port))
    (assoc this :http-server (run-jetty app {:host host :port port :join? false})))

  (stop [this]
    (println ";; Stopping web server")
    (.stop (:http-server this))
    this))

(defn new-server [host port app]
  (map->Server {:host host :port port :app app}))

(defn main-system [{:keys [host port app]}]
  (component/system-map
   :server (new-server host port app)))
