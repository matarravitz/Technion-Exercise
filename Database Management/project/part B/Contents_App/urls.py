from django.urls import path
from . import views

urlpatterns = [
    path('', views.index, name='index'),
    path('QueryResults', views.QueryResults, name='QueryResults'),
    path('AddActorToMovie', views.AddActorToMovie, name='AddActorToMovie'),
    path('viewDocumentation', views.viewDocumentation, name='viewDocumentation')

]