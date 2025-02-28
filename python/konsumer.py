import threading
from confluent_kafka import Consumer
from confluent_kafka.schema_registry.avro import AvroDeserializer
from confluent_kafka.schema_registry import SchemaRegistryClient

from shared_var import package_and_rider_records

# Configuración del cliente del registro de esquemas
schema_registry_conf = {'url': 'http://localhost:8081'}
schema_registry_client = SchemaRegistryClient(schema_registry_conf)
avro_deserializer = AvroDeserializer(schema_registry_client)

def consume():
    global package_and_rider_records
    consumer_conf = {
        'bootstrap.servers': 'localhost:9092',
        'group.id': 'stats_reader',
        'auto.offset.reset': 'earliest'
    }
    consumer = Consumer(consumer_conf)
    consumer.subscribe(["package-and-rider"])

    try:
        while True:
            msg = consumer.poll(1.5)
            if msg is None:
                continue
            if msg.error():
                print(f"Error: {msg.error()}")
            else:
                value = avro_deserializer(msg.value(), None)
                if value:
                    print(f"New record: {value}")
                    raw_eta = value.get('destiny_eta', '')

                    new_row = {
                        'package_id': value.get('package_id'),
                        'rider_name': value.get('rider_name'),
                        'previous_warehouse': value.get('previous_warehouse'),
                        'actual_warehouse': value.get('actual_warehouse'),
                        'destiny_warehouse': value.get('destiny_warehouse'),
                        'status': value.get('status'),
                        'destiny_eta': raw_eta
                    }
                    existing_record = next((record for record in package_and_rider_records if record['package_id'] == new_row['package_id']), None)
                    if existing_record:
                        existing_record.update(new_row)
                    else:
                        package_and_rider_records.append(new_row)
                    print(f"Records: {package_and_rider_records}")
    finally:
        consumer.close()

# Iniciar el consumidor en un hilo separado
consumer_thread = threading.Thread(target=consume, daemon=True)
consumer_thread.start()