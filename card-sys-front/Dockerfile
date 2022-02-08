FROM node:16.13.0-alpine as build




COPY . /card-sys-front

WORKDIR /card-sys-front



# Add the source code to app


RUN npm install
RUN npm install -g @angular/cli@13.2.2

RUN npm run build


FROM nginx:1.17.10-alpine

# Copy the build output to replace the default nginx contents.
COPY --from=build /card-sys-front/dist/card-sys-front /usr/share/nginx/html
EXPOSE 4200


