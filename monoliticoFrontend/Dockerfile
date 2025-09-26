FROM nginx:alpine

COPY ./nginx /etc/nginx
COPY ./dist /usr/share/nginx/html

EXPOSE 3000

CMD ["nginx", "-g", "daemon off;"]