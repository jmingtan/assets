(ns assets.app
  (:require [ring.middleware.multipart-params :refer [wrap-multipart-params]]
            [assets.handler :refer [handler]]))

(def app (-> handler
             wrap-multipart-params))
