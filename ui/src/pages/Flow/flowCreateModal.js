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
    flowApi.getAllFlowType().then((resp) => {
      if (resp.success) {
        setAllFlowType(resp.data);
      }
    });

    flowApi.getAllInbound().then((resp) => {
      if (resp.success) {
        setAllInbound(resp.data);
      }
    });

    flowApi.getAllParserType().then((resp) => {
      if (resp.success) {
        setAllParser(resp.data);
      }
    });
    flowApi.getAllKeyMapper().then((resp) => {
      if (resp.success) {
        setAllKeyMapper(resp.data);
      }
    });
    flowApi.getAllFormatter().then((resp) => {
      if (resp.success) {
        setAllFormatter(resp.data);
      }
    });
    flowApi.getOutboundName().then((resp) => {
      if (resp.success) {
        setAllOutbound(resp.data);
      }
    });
  }, []);

  const doCreateNewFlow = (values) => {
    setConfirmLoading(true);
    flowApi.createNew(values).then((resp) => {
      if (resp.success) {
        message.success('operate successfully!');
        props.closeCreateModal(true);
      }
       setConfirmLoading(false);
    });

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
              name="transactionType"
              label="Transaction"
              //labelAlign="right"
              rules={[{ required: true, message: 'Missing Flow Type' }]}
            >
              <Select options={[...allFlowType]} style={{ width: 300 }} />
            </Form.Item>

            <Form.Item
              name="inboundConfigId"
              label="Inbound Name"
              //labelAlign="right"
              rules={[{ required: true, message: 'Missing name' }]}
            >
              <Select options={[...allInbound]} style={{ width: 300 }} />
            </Form.Item>

            <Form.Item
              name="parserType"
              label="Parser Type"
              //labelAlign="right"
              rules={[{ required: true, message: 'Missing Parser Type' }]}
            >
              <Select options={[...allParser]} style={{ width: 300 }} />
            </Form.Item>

            <Form.Item
              name="keyMapperId"
              label="KeyMapper Name"
              // labelAlign="right"
              rules={[{ required: true, message: 'Missing KeyMapper Name' }]}
            >
              <Select options={[...allKeyMapper]} style={{ width: 300 }} />
            </Form.Item>

            <Form.Item
              name="formatterId"
              label="Formatter Name"
              //labelAlign="right"
              rules={[{ required: true, message: 'Missing Formatter Name' }]}
            >
              <Select options={[...allFormatter]} style={{ width: 300 }} />
            </Form.Item>

            <Form.Item
              name="outboundConfigId"
              label="Outbound Name"
              //labelAlign="right"
              rules={[{ required: true, message: 'Missing Outbound Name' }]}
            >
              <Select options={[...allOutbound]} style={{ width: 300 }} />
            </Form.Item>
          </Form>
        </div>
      </Modal>
    </>
  );
};

export default FlowCreateModal;
