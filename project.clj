(defproject myblog "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.6"]
                 [com.datomic/datomic-pro "0.9.4609"]
                 [datomic-schema "1.0.2"]
                 [hiccup "1.0.5"]
                 [enlive "1.1.5"]
                 [ring-basic-authentication "1.0.2"]]
  :jvm-opts ^:replace ["-Xms1g" "-Xmx1g" "-server"]
  :plugins [[lein-ring "0.8.10"]]
  :ring {:handler myblog.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}})
