(defproject sv/gcloud.storage.concat "0.1.1"
  :description "Concatenates files on Google Storage into one file."
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [sv/gcloud.storage "0.2.0"]
                 [sv/gcloud.client "0.1.3"]
                 [me.raynes/fs "1.4.6"]])
