FROM java:8

EXPOSE 6080

# It is havy operation. Should be first.
# Brings files to /var/log/fleettracker

RUN curl -SL http://github.com/tikalk/as-gps-parent/raw/master/gps-file.tar | tar -xvf -

#ADD http://github.com/tikalk/as-gps-parent/raw/master/gps-file.tar /
#RUN tar -xvf gps-file.tar

#ADD gps-file.tar /

# Copy your fat jar to the container
ADD build/distributions/as-gps-firehose-1.0.0.tar.gz /as-gps-firehose

RUN mkdir -p /var/log/fleettracker

# Launch the verticle
ENV WORKDIR /as-gps-firehose
ENTRYPOINT ["sh", "-c"]
CMD ["cd $WORKDIR ; ./run-bin/gps-firehose.sh"]
