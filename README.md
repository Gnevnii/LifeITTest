# LifeITTest
Точка запуска - класс Main.
Исходные парамеры:
количество звонящих - 1000
масимальное количество принимаемых звонков - 100
количество водителей - 10

При старте во временной папке ОС создает папка "темповая_папка/messages". В ней создаются xml-файлы, имитирующие запросы звонящих.
По мере выполнения приложения - создаются папки "темповая_папка/taxi_drivers", "темповая_папка/taxi_drivers/такси-0",..., "темповая_папка/taxi_drivers/такси-9".
В папках, соответствующих каждому таксисту, по мере выполнения приложения создаются xml-файлы, соответствующие выполненному данным таксистом запросу.

Недостатки реализации:
- пулы не останавливаются после завершения работы всех потоков (стоит предусмотреть shutdown);
- система логирования простейшая, для визуализации выполнения приложения (стоит расширить логирование, добавить информативности);
- в ОС остаются лишние файлы (при завершении стоит предусмотреть удаление лишних файлов).