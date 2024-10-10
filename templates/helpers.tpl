{{- define "scale-spring-app.name" -}}
{{ .Chart.Name }}
{{- end -}}

{{- define "scale-spring-app.fullname" -}}
{{ .Release.Name }}-{{ .Chart.Name }}
{{- end -}}
