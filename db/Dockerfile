FROM mongo:3.6

COPY entrypoint.sh /entrypoint.sh
COPY brainheap_db_starter /brainheap_db_starter

EXPOSE 27017

CMD ["/entrypoint.sh"]