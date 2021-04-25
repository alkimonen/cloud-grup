import time
from locust import HttpUser, task, between

class WebsiteUser(HttpUser):
    wait_time = between(1, 5)

    @task
    def redirect(self):
        self.client.get(url="/google")

    @task
    def getInfo(self):
        self.client.get(url="/entry/info/google")

    @task
    def newEntry(self):
        self.client.post(url="/entry/new", data="www.google.com")
