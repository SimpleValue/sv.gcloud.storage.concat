(ns sv.gcloud.storage.concat.log-files
  (:require [sv.gcloud.storage.client :as c]
            [clojure.java.io :as io]
            [sv.gcloud.client :as g]))

(defn- create-client []
  (g/create-client
   {:scopes ["https://www.googleapis.com/auth/devstorage.read_write"]}))

(defn concat-log-files [params]
  (let [{:keys [client input-files output-file separator gzip]
         :or {separator "\n"
              client (create-client)}} params
        tmp-file (java.io.File/createTempFile "tmp" "file")]
    (try
      (with-open [out (if gzip
                        (java.util.zip.GZIPOutputStream.
                         (io/output-stream tmp-file))
                        (io/output-stream tmp-file))]
        (doseq [input-file input-files]
          (let [input-stream (c/stream-object client input-file)]
            (io/copy input-stream out)
            (when separator
              (io/copy (java.io.ByteArrayInputStream.
                        (.getBytes separator "UTF-8"))
                       out)))))
      (client (c/upload-request
               (assoc
                output-file
                :content tmp-file)))
      true
      (finally
        (.delete tmp-file)))))

(comment
  ;; example:
  (concat-log-files
   {:input-files [{:bucket "your-bucket"
                   :name "input1.log"}
                  {:bucket "your-bucket"
                   :name "input2.log"}]
    :output-file {:bucket "your-bucket"
                  :name "output.gz"}
    :gzip true})
  )
