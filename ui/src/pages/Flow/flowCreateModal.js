import {
  Space,
  Table,
  Tag,
  Button,
  Col,
  Form,
  Input,
  Row,
  Select,
  UpOutlined,
  DownOutlined,
  message,
  Popconfirm,
  Modal,
} from 'antd';
import React, { useState, useEffect } from 'react';
import flowApi from '@/services/spark/flowApi';

const FlowCreateModal = (props) => {
  const [confirmLoading, setConfirmLoading] = useState(false);

  const [allInbound, setAllInbound] = useState([]);

  const [allOutbound, setAllOutbound] = useState([]);

  const [allParser, setAllParser] = useState([]);

  const [allKeyMapper, setAllKeyMapper] = useState([]);

  const [allFormatter, setAllFormatter] = useState([]);

  const [allFlowType, setAllFlowType] = useState([]);

  const [form] = Form.useForm();

  const handleOk = () => {
    form.submit();
  };

  const handleCancel = () => {
    props.closeCreateModal();
  };

  useEffect(() => {
    flowApi.getAllFlowType().then((data) => {
      setAllFlowType(data);
    });

    flowApi.getAllInbound().then((data) => {
      setAllInbound(data);
    });

    flowApi.getAllParserType().then((data) => {
      setAllParser(data);
    });
    flowApi.getAllKeyMapper().then((data) => {
      setAllKeyMapper(data);
    });
    flowApi.getAllFormatter().then((data) => {
      setAllFormatter(data);
    });
    flowApi.getOutboundName().then((data) => {
      setAllOutbound(data);
    });
  }, []);

  const doCreateNewFlow = (values) => {
    setConfirmLoading(true);
    debugger;
    message.success('operate successfully!');
    setTimeout(() => {
      setConfirmLoading(false);
      props.closeCreateModal();
    }, 2000);
  };

  return (
    <>
      <Modal
        title="Create New Flow"
        //open={open}
        visible={true}
        onOk={handleOk}
        confirmLoading={confirmLoading}
        onCancel={handleCancel}
      >
        <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
          <Form
            form={form}
            name="advanced_search"
            className="ant-advanced-search-form"
            layout="vertical"
            onFinish={doCreateNewFlow}
            labelAlign="right"
          >
            <Form.Item
              name="name"
              label="Name"
              //labelAlign="right"
              rules={[{ required: true, message: 'Missing name' }]}
            >
              <Input style={{ width: 300 }} />
            </Form.Item>

            <Form.Item
              name="type"
              label="Flow Type"
              //labelAlign="right"
              rules={[{ required: true, message: 'Missing Flow Type' }]}
            >
              <Select
                options={[{ label: 'Default', value: 'default' }, ...allFlowType]}
                style={{ width: 300 }}
              />
            </Form.Item>

            <Form.Item
              name="inboundName"
              label="Inbound Name"
              //labelAlign="right"
              rules={[{ required: true, message: 'Missing name' }]}
            >
              <Select
                options={[
                  { label: 'JDBC Inbound', value: 'JDBC Inbound' },
                  { label: 'Kafka Inbound', value: 'Kafka Inbound' },
                  ...allInbound,
                ]}
                style={{ width: 300 }}
              />
            </Form.Item>

            <Form.Item
              name="parserType"
              label="Parser Type"
              //labelAlign="right"
              rules={[{ required: true, message: 'Missing Parser Type' }]}
            >
              <Select
                options={[
                  { label: 'JSON', value: 'JSON' },
                  { label: 'XML', value: 'XML' },
                  { label: 'KeyValue', value: 'KeyValue' },
                  { label: 'String', value: 'String' },
                  ...allParser,
                ]}
                style={{ width: 300 }}
              />
            </Form.Item>

            <Form.Item
              name="keyMapperName"
              label="KeyMapper Name"
              // labelAlign="right"
              rules={[{ required: true, message: 'Missing KeyMapper Name' }]}
            >
              <Select
                options={[{ label: 'Key-Value', value: 'Key-Value' }, ...allKeyMapper]}
                style={{ width: 300 }}
              />
            </Form.Item>

            <Form.Item
              name="formatterName"
              label="Formatter Name"
              //labelAlign="right"
              rules={[{ required: true, message: 'Missing Formatter Name' }]}
            >
              <Select
                options={[
                  { label: 'JSON', value: 'JSON' },
                  { label: 'XML', value: 'XML' },
                  { label: 'KeyValue', value: 'KeyValue' },
                  { label: 'String', value: 'String' },
                  allFormatter,
                ]}
                style={{ width: 300 }}
              />
            </Form.Item>

            <Form.Item
              name="outboundName"
              label="Outbound Name"
              //labelAlign="right"
              rules={[{ required: true, message: 'Missing Outbound Name' }]}
            >
              <Select
                options={[
                  { label: 'JDBC Inbound', value: 'JDBC Inbound' },
                  { label: 'Kafka Inbound', value: 'Kafka Inbound' },
                  ...allOutbound,
                ]}
                style={{ width: 300 }}
              />
            </Form.Item>
          </Form>
        </div>
      </Modal>
    </>
  );
};

export default FlowCreateModal;
