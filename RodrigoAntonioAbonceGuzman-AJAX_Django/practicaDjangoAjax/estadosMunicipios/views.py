from django.shortcuts import render
from .models import Estado
from django.http import JsonResponse
from .models import Municipio

def index(request):
    estados = Estado.objects.all()
    return render(request, 'index.html', {'estados': estados})


def cargar_municipios(request):
    estado_id = request.GET.get('estado_id')
    municipios = Municipio.objects.filter(estado_id=estado_id).all()
    municipios_list = [{"id": municipio.id, "nombre": municipio.nombre} for municipio in municipios]
    return JsonResponse(municipios_list, safe=False)
