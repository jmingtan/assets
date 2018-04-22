(ns assets.files
  (:require [amazonica.aws.s3 :as s3]
            [ring.util.codec :refer [url-encode]]
            [config.core :refer [env]]))

(def creds (select-keys env [:access-key :secret-key :endpoint]))

(def bucket (:bucket env))

(defn format-filename [filename region bucket]
  (format "https://s3-%s.amazonaws.com/%s/%s"
          region
          bucket
          (url-encode filename)))

(defn list-files []
  (let [result (s3/list-objects-v2 creds {:bucket-name bucket})
        region (s3/get-bucket-location creds bucket)
        summaries (:object-summaries result)]
    (map (fn [summary]
           {:name (:key summary)
            :last-modified (:last-modified summary)
            :size (:size summary)
            :url (format-filename (:key summary) region bucket)})
         summaries)))

(defn upload-file [name file content-type]
  (s3/put-object creds {:bucket-name bucket
                        :key name
                        :file file
                        :metadata {:content-type content-type}
                        :access-control-list {:grant-permission ["AllUsers" "Read"]}}))
